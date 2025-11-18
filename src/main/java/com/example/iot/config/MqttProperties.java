package com.example.iot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String brokerUrl;

    private String clientId;

    private List<String> topics = new ArrayList<>();

    private String username = "";

    private String password = "";
}
