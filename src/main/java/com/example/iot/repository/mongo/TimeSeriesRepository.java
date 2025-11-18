package com.example.iot.repository.mongo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.iot.model.mongo.TimeSeriesData;

public interface TimeSeriesRepository extends MongoRepository<TimeSeriesData, String> {

    List<TimeSeriesData> findByDeviceIdAndTimestampBetween(String deviceId, Instant from, Instant to);
}
