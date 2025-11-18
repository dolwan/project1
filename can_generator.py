import json
import random
import time
from typing import Dict, Any


class CANDataGenerator:
    """生成周期性 CAN 样式的遥测数据。"""

    def __init__(self, vin: str) -> None:
        # 车辆唯一 VIN，用于组装消息
        self.vin = vin

    def generate_payload(self, seq: int) -> Dict[str, Any]:
        """生成包含 CRC（异或校验）的遥测载荷。"""
        timestamp = time.time()
        data = {
            "vin": self.vin,
            "ts": timestamp,
            "seq": seq,
            "motor_speed": self._generate_motor_speed(),
            "battery_voltage": self._generate_battery_voltage(),
        }
        return self.add_crc(data)

    def add_crc(self, payload: Dict[str, Any]) -> Dict[str, Any]:
        # 复制一份，避免修改原始数据
        enriched = dict(payload)
        enriched["crc"] = self._compute_crc(enriched)
        return enriched

    @staticmethod
    def _generate_motor_speed() -> int:
        # 转速范围 800 ~ 4500rpm，简单随机模拟
        return random.randint(800, 4500)

    @staticmethod
    def _generate_battery_voltage() -> float:
        # 电压范围 320V ~ 430V，保留两位小数
        return round(random.uniform(320.0, 430.0), 2)

    @staticmethod
    def _compute_crc(payload: Dict[str, Any]) -> int:
        """对序列化后的数据执行逐字节异或，得到简单 CRC。"""
        crc_source = {key: value for key, value in payload.items() if key != "crc"}
        serialized = json.dumps(crc_source, sort_keys=True, separators=(",", ":"))
        crc = 0
        for byte in serialized.encode("utf-8"):
            crc ^= byte
        return crc
