package com.example.iot.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.iot.model.mongo.TimeSeriesData;
import com.example.iot.service.TimeSeriesService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/timeseries")
@Validated
public class TimeSeriesController {

    private final TimeSeriesService timeSeriesService;

    public TimeSeriesController(TimeSeriesService timeSeriesService) {
        this.timeSeriesService = timeSeriesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeSeriesData insert(@Valid @RequestBody CreateTimeSeriesRequest request) {
        TimeSeriesData data = new TimeSeriesData();
        data.setDeviceId(request.deviceId());
        data.setTimestamp(request.timestamp());
        data.setMetrics(request.metrics());
        return timeSeriesService.save(data);
    }

    @GetMapping
    public List<TimeSeriesData> query(
            @RequestParam @NotBlank String deviceId,
            @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) Instant from,
            @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) Instant to
    ) {
        return timeSeriesService.query(deviceId, from, to);
    }

    public record CreateTimeSeriesRequest(
            @NotBlank String deviceId,
            @NotNull Instant timestamp,
            @NotNull java.util.Map<String, Object> metrics
    ) {
    }
}
