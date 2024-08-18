package com.fhdo.mqttclient;

import com.fhdo.mqttclient.client.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MqttPublisherRunner implements CommandLineRunner {

    private final MqttPublisher mqttPublisher;
    @Override
    public void run(String... args) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(mqttPublisher::sendMessage, 0, 1, TimeUnit.SECONDS);
    }
}
