import logging
from typing import Dict


class EventLogger:
    """负责记录遥测与 DTC 相关日志。"""

    def __init__(self, log_file: str = "vehicle.log") -> None:
        # 初始化 logger，只创建一次 handler，避免重复写入
        self.logger = logging.getLogger("vehicle-simulator")
        self.logger.setLevel(logging.INFO)
        if not self.logger.handlers:
            handler = logging.FileHandler(log_file)
            formatter = logging.Formatter(
                "%(asctime)s - %(levelname)s - %(message)s",
                datefmt="%Y-%m-%d %H:%M:%S",
            )
            handler.setFormatter(formatter)
            self.logger.addHandler(handler)

    def log_dtc(self, dtc_event: Dict[str, object]) -> None:
        # 将故障码事件以 warning 级别记录
        self.logger.warning(
            "DTC triggered for VIN %s (seq=%s): %s - %s",
            dtc_event.get("vin"),
            dtc_event.get("seq"),
            dtc_event.get("dtc_code"),
            dtc_event.get("description"),
        )

    def log_publish_failure(self, payload: Dict[str, object]) -> None:
        # 弱网掉包时，提示已进入重传队列
        self.logger.error("MQTT publish queued due to weak network: seq=%s", payload.get("seq"))

    def log_publish_success(self, payload: Dict[str, object]) -> None:
        # 上报成功时记录，用于统计发送节奏
        self.logger.info("MQTT publish success: seq=%s", payload.get("seq"))
