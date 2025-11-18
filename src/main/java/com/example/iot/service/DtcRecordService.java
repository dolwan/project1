package com.example.iot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.iot.model.mysql.DtcRecord;
import com.example.iot.repository.mysql.DtcRecordRepository;

@Service
public class DtcRecordService {

    private final DtcRecordRepository dtcRecordRepository;

    public DtcRecordService(DtcRecordRepository dtcRecordRepository) {
        this.dtcRecordRepository = dtcRecordRepository;
    }

    public DtcRecord save(DtcRecord dtcRecord) {
        return dtcRecordRepository.save(dtcRecord);
    }

    public List<DtcRecord> findByDeviceId(String deviceId) {
        return dtcRecordRepository.findByDeviceId(deviceId);
    }
}
