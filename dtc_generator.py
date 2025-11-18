import random
import time
from typing import Dict, Optional


class DTCGenerator:
    """随机触发诊断故障码（DTC）事件。"""

    def __init__(self, probability: float = 0.05) -> None:
        # 触发概率，可通过构造参数灵活调节
        self.probability = probability
        # 常见新能源车故障码列表
        self.codes = [
            ("P0A1F", "Drive motor inverter fault"),
            ("P0A94", "DC/DC converter performance"),
            ("P1A10", "Battery cooling pump control"),
        ]

    def maybe_trigger(self, vin: str, seq: int) -> Optional[Dict[str, object]]:
        # 按概率决定是否生成 DTC 事件
        if random.random() > self.probability:
            return None

        code, description = random.choice(self.codes)
        return {
            "vin": vin,
            "ts": time.time(),
            "seq": seq,
            "dtc_code": code,
            "description": description,
        }
