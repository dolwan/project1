package com.example.iot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.iot.model.mysql.AlarmEvent;
import com.example.iot.repository.mysql.AlarmEventRepository;

@Service
public class AlarmEventService {

    private final AlarmEventRepository alarmEventRepository;

    public AlarmEventService(AlarmEventRepository alarmEventRepository) {
        this.alarmEventRepository = alarmEventRepository;
    }

    public AlarmEvent save(AlarmEvent event) {
        return alarmEventRepository.save(event);
    }

    public List<AlarmEvent> findByDeviceId(String deviceId) {
        return alarmEventRepository.findByDeviceId(deviceId);
    }
}
