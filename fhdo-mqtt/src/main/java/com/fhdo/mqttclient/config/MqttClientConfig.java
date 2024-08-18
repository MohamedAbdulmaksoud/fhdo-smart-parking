package com.fhdo.mqttclient.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MqttClientConfig {

    private static final String MOSQUITTO_URI = "tcp://localhost:1883";
    private static final String CLIENT_ID = "ParkingSimulator";

    @Bean
    MqttAsyncClient mqttAsyncClient() throws MqttException {
        final var client = new MqttAsyncClient(MOSQUITTO_URI, CLIENT_ID);
        IMqttToken token = client.connect();
        token.waitForCompletion();
        String topic = "MqttClientConfig";
        String content = "Start Message from MqttClientConfig";
        MqttMessage message = new MqttMessage(content.getBytes());
        token = client.publish(topic, message);
        token.waitForCompletion();
        log.info("[{}] connected to broker at {}", CLIENT_ID, MOSQUITTO_URI);
        return client;
    }
}
