CREATE DATABASE IF NOT EXISTS iot_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE iot_app;

CREATE TABLE IF NOT EXISTS dtc_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vin VARCHAR(32) NOT NULL,
    code VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    occurred_at TIMESTAMP NOT NULL,
    INDEX idx_vin_occurred_at (vin, occurred_at)
);

CREATE TABLE IF NOT EXISTS alarm_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(64) NOT NULL,
    severity VARCHAR(32) NOT NULL,
    description VARCHAR(255),
    occurred_at TIMESTAMP NOT NULL,
    status VARCHAR(32) DEFAULT 'OPEN',
    INDEX idx_type_status (type, status)
);
