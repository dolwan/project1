# Spring Boot 3.x 物联网数据平台骨架

该工程示例集成 MQTT 客户端订阅、MongoDB 时序数据存储，以及 MySQL 结构化数据存储，采用 controller / service / repository / config / model 分层结构，便于快速扩展业务能力。

## 主要特性
- **MQTT 客户端订阅**：使用 Spring Integration MQTT 作为客户端，支持多 topic 订阅，可在 `MqttMessageService` 中集中处理消息。
- **MongoDB**：通过 `spring-boot-starter-data-mongodb` 存储时序类数据（如传感器上报）。
- **MySQL**：通过 `spring-boot-starter-data-jpa` 管理结构化数据（如 DTC、告警事件）。
- **分层结构**：controller / service / repository / config / model，便于演进。

## 启动示例
1. 调整 `src/main/resources/application.yml` 中的 MQTT、MongoDB、MySQL 连接信息。
2. 启动应用：
   ```bash
   mvn spring-boot:run
   ```
3. 示例接口：
   - `POST /api/timeseries` 写入时序数据。
   - `GET /api/timeseries?deviceId=xxx&from=...&to=...` 查询时序数据。
   - `POST /api/events/alarms`、`POST /api/events/dtc` 写入告警/DTC。

## 使用 docker-compose 启动依赖服务
项目根目录已提供 `docker-compose.yml`，包含 EMQX MQTT Broker、MongoDB、MySQL，均配置了持久化卷和固定网络：

```bash
# 启动依赖服务
docker compose up -d

# 查看 EMQX 控制台（默认账号/密码：admin / public）
# http://localhost:18083
```

- MQTT Broker：1883（TCP）、8083（WebSocket）、18083（Dashboard）
- MongoDB：27017（默认 root 用户 mongo / mongoPass123!，库 iot_timeseries，authSource=admin）
- MySQL：3306（root 密码 RootPass123!；业务账号 iot_user / UserPass123!，库 iot_app）

## application.yml 示例
```yaml
spring:
  application:
    name: iot-platform
  data:
    mongodb:
      uri: mongodb://mongo:mongoPass123!@localhost:27017/iot_timeseries?authSource=admin
  datasource:
    url: jdbc:mysql://localhost:3306/iot_app?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: iot_user
    password: UserPass123!
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

mqtt:
  broker-url: tcp://localhost:1883
  client-id: iot-platform
  topics:
    - telemetry/+/data
    - telemetry/+/event
  username: ""
  password: ""
```

## Maven 依赖（pom.xml 摘要）
- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-data-mongodb`
- `spring-integration-mqtt`
- `mysql-connector-j`
- `lombok`

## 推荐目录结构
```
├── pom.xml
├── README.md
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── iot
│       │               ├── Application.java
│       │               ├── config
│       │               │   ├── MqttConfig.java
│       │               │   └── MqttProperties.java
│       │               ├── controller
│       │               │   ├── EventController.java
│       │               │   └── TimeSeriesController.java
│       │               ├── model
│       │               │   ├── mongo
│       │               │   │   └── TimeSeriesData.java
│       │               │   └── mysql
│       │               │       ├── AlarmEvent.java
│       │               │       └── DtcRecord.java
│       │               ├── repository
│       │               │   ├── mongo
│       │               │   │   └── TimeSeriesRepository.java
│       │               │   └── mysql
│       │               │       ├── AlarmEventRepository.java
│       │               │       └── DtcRecordRepository.java
│       │               └── service
│       │                   ├── AlarmEventService.java
│       │                   ├── DtcRecordService.java
│       │                   ├── MqttMessageService.java
│       │                   └── TimeSeriesService.java
│       └── resources
│           └── application.yml
└── target (构建后生成)
```

> 可以在 `MqttMessageService#handleInbound` 中对 MQTT 消息进行解析，并调用对应的服务将数据写入 MongoDB 或 MySQL。
