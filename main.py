import signal
import time
from threading import Event

from can_generator import CANDataGenerator
from dtc_generator import DTCGenerator
from logger import EventLogger
from mqtt_client import WeakNetworkMQTTClient


class VehicleSimulator:
    """整车仿真器，负责生成遥测、触发 DTC 并通过 MQTT 上报。"""

    def __init__(
        self,
        vin: str,
        broker_host: str = "localhost",
        broker_port: int = 1883,
        weak_network_drop: float = 0.1,
    ) -> None:
        self.vin = vin
        self.can_generator = CANDataGenerator(vin)
        self.dtc_generator = DTCGenerator()
        self.logger = EventLogger()
        # 初始化具备弱网处理能力的 MQTT 客户端
        self.mqtt_client = WeakNetworkMQTTClient(
            broker_host=broker_host,
            broker_port=broker_port,
            vin=vin,
            weak_network_drop=weak_network_drop,
        )
        # 序列号自增，用于数据有序性
        self.seq = 0
        # 通过事件对象优雅退出
        self.stop_event = Event()

    def start(self) -> None:
        # 建立 MQTT 连接并注册退出信号
        self.mqtt_client.connect()
        signal.signal(signal.SIGINT, self._handle_stop)
        signal.signal(signal.SIGTERM, self._handle_stop)
        self._run_loop()

    def _run_loop(self) -> None:
        while not self.stop_event.is_set():
            self.seq += 1
            payload = self.can_generator.generate_payload(self.seq)
            published = self.mqtt_client.publish(payload)
            if published:
                self.logger.log_publish_success(payload)
            else:
                self.logger.log_publish_failure(payload)

            dtc_event = self.dtc_generator.maybe_trigger(self.vin, self.seq)
            if dtc_event:
                self.logger.log_dtc(dtc_event)
                dtc_payload = self.can_generator.add_crc(dtc_event)
                self.mqtt_client.publish(dtc_payload)

            time.sleep(0.1)

    def _handle_stop(self, signum, frame) -> None:  # type: ignore[override]
        self.stop_event.set()
        self.mqtt_client.disconnect()


if __name__ == "__main__":
    simulator = VehicleSimulator(vin="TESTVIN1234567890")
    simulator.start()
