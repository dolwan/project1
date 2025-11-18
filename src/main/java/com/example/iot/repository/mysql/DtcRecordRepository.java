package com.example.iot.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iot.model.mysql.DtcRecord;

public interface DtcRecordRepository extends JpaRepository<DtcRecord, Long> {

    List<DtcRecord> findByDeviceId(String deviceId);
}
