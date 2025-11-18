package com.example.iot.model.mongo;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "time_series")
public class TimeSeriesData {

    @Id
    private String id;

    private String deviceId;

    private Instant timestamp;

    private Map<String, Object> metrics;
}
