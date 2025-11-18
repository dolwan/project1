package com.example.iot.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iot.model.mysql.AlarmEvent;

public interface AlarmEventRepository extends JpaRepository<AlarmEvent, Long> {

    List<AlarmEvent> findByDeviceId(String deviceId);
}
