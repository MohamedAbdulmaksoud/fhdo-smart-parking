package com.fhdo.mqttclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqttPublisher {

    private final MqttAsyncClient mqttAsyncClient;

    private final MessageGenerator messageGenerator;

    private final ObjectMapper objectMapper;

    public void sendMessage() {
        ParkingSensorMessage message = messageGenerator.generateMessage();
        String topic = "%s:%s".formatted(message.getMunicipality(), message.getParkingId());
        MqttMessage mqttMessage;
        try {
            mqttMessage = new MqttMessage(objectMapper.writeValueAsBytes(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            IMqttToken token = mqttAsyncClient.publish(topic, mqttMessage);
            token.waitForCompletion();
            log.info("Published Message to {}",topic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void preDestroy() throws MqttException {
        if (mqttAsyncClient.isConnected())
            mqttAsyncClient.close();
    }

}
