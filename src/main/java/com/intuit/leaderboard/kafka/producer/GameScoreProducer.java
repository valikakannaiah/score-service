package com.intuit.leaderboard.kafka.producer;

import com.intuit.leaderboard.models.entities.kafka.KafkaPlayerScoreEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameScoreProducer {

    private final KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.topic-player-score.name}")
    private String topic;

    public void publishScore(KafkaPlayerScoreEvent kafkaPlayerScoreEvent){
        Message<KafkaPlayerScoreEvent> message = MessageBuilder.withPayload(kafkaPlayerScoreEvent)
                .setHeader(KafkaHeaders.TOPIC,topic)
                .build();
        kafkaTemplate.send(message);
        log.info("Kafka topic: {}, message: {} ", topic, message);
    }
}
