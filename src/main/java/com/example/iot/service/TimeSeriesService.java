package com.example.iot.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.iot.model.mongo.TimeSeriesData;
import com.example.iot.repository.mongo.TimeSeriesRepository;

@Service
public class TimeSeriesService {

    private final TimeSeriesRepository timeSeriesRepository;

    public TimeSeriesService(TimeSeriesRepository timeSeriesRepository) {
        this.timeSeriesRepository = timeSeriesRepository;
    }

    public TimeSeriesData save(TimeSeriesData data) {
        return timeSeriesRepository.save(data);
    }

    public List<TimeSeriesData> query(String deviceId, Instant from, Instant to) {
        return timeSeriesRepository.findByDeviceIdAndTimestampBetween(deviceId, from, to);
    }
}
