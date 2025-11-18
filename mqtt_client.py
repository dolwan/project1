import json
import random
import threading
from collections import deque
from typing import Deque, Dict

import paho.mqtt.client as mqtt


class WeakNetworkMQTTClient:
    """支持弱网缓存与重传的 MQTT 客户端封装。"""

    def __init__(
        self,
        broker_host: str,
        broker_port: int,
        vin: str,
        weak_network_drop: float = 0.1,
        topic_template: str = "vehicle/{vin}/telemetry",
    ) -> None:
        self.vin = vin
        # 主题格式：vehicle/{vin}/telemetry
        self.topic = topic_template.format(vin=vin)
        self.client = mqtt.Client(client_id=f"sim-{vin}")
        self.client.on_connect = self._on_connect
        self.client.on_disconnect = self._on_disconnect
        self.client.on_publish = self._on_publish

        self.broker_host = broker_host
        self.broker_port = broker_port
        self.connected = False
        self.lock = threading.Lock()
        # 使用 deque 作为缓存队列，保证 FIFO 顺序
        self.queue: Deque[str] = deque()
        # 弱网模拟掉包概率
        self.weak_network_drop = weak_network_drop

    def connect(self) -> None:
        # 启动异步循环确保回调生效
        self.client.connect(self.broker_host, self.broker_port, keepalive=60)
        self.client.loop_start()

    def disconnect(self) -> None:
        self.client.loop_stop()
        self.client.disconnect()

    def publish(self, payload: Dict[str, object]) -> bool:
        serialized = json.dumps(payload, separators=(",", ":"))
        with self.lock:
            # 模拟弱网：随机掉包则入队待重传
            if self._should_drop():
                self.queue.append(serialized)
                return False

            # 未连接成功时也先入队
            if not self.connected:
                self.queue.append(serialized)
                return False

            return self._publish_serialized(serialized)

    def drain_queue(self) -> None:
        with self.lock:
            while self.queue and self.connected:
                serialized = self.queue.popleft()
                self._publish_serialized(serialized)

    def _publish_serialized(self, serialized: str) -> bool:
        result = self.client.publish(self.topic, serialized, qos=1)
        return result.rc == mqtt.MQTT_ERR_SUCCESS

    def _should_drop(self) -> bool:
        if self.weak_network_drop <= 0:
            return False
        return random.random() < self.weak_network_drop

    def _on_connect(self, client: mqtt.Client, userdata, flags, rc) -> None:  # type: ignore[override]
        self.connected = rc == 0
        if self.connected:
            # 连接恢复后立即清空队列
            self.drain_queue()

    def _on_disconnect(self, client: mqtt.Client, userdata, rc) -> None:  # type: ignore[override]
        self.connected = False

    def _on_publish(self, client: mqtt.Client, userdata, mid) -> None:  # type: ignore[override]
        # Placeholder for potential metrics
        return
