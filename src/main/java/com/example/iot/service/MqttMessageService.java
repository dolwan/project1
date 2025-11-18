package com.example.iot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MqttMessageService {

    private static final Logger log = LoggerFactory.getLogger(MqttMessageService.class);

    public void handleInbound(Message<?> message) {
        // TODO: parse payload and persist to MongoDB/MySQL as needed
        log.info("Received MQTT message: topic={}, payload={}", message.getHeaders().get("mqtt_receivedTopic"), message.getPayload());
    }
}
