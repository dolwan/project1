package com.example.iot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.iot.model.mysql.AlarmEvent;
import com.example.iot.model.mysql.DtcRecord;
import com.example.iot.service.AlarmEventService;
import com.example.iot.service.DtcRecordService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/events")
@Validated
public class EventController {

    private final AlarmEventService alarmEventService;
    private final DtcRecordService dtcRecordService;

    public EventController(AlarmEventService alarmEventService, DtcRecordService dtcRecordService) {
        this.alarmEventService = alarmEventService;
        this.dtcRecordService = dtcRecordService;
    }

    @PostMapping("/alarms")
    @ResponseStatus(HttpStatus.CREATED)
    public AlarmEvent createAlarm(@Valid @RequestBody AlarmEvent alarmEvent) {
        return alarmEventService.save(alarmEvent);
    }

    @GetMapping("/alarms/{deviceId}")
    public List<AlarmEvent> findAlarms(@PathVariable @NotBlank String deviceId) {
        return alarmEventService.findByDeviceId(deviceId);
    }

    @PostMapping("/dtc")
    @ResponseStatus(HttpStatus.CREATED)
    public DtcRecord createDtc(@Valid @RequestBody DtcRecord dtcRecord) {
        return dtcRecordService.save(dtcRecord);
    }

    @GetMapping("/dtc/{deviceId}")
    public List<DtcRecord> findDtc(@PathVariable @NotBlank String deviceId) {
        return dtcRecordService.findByDeviceId(deviceId);
    }
}
