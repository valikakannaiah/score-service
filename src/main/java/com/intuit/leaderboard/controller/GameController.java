package com.intuit.leaderboard.controller;

import com.intuit.leaderboard.kafka.producer.GameScoreProducer;
import com.intuit.leaderboard.models.entities.kafka.KafkaPlayerScoreEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.intuit.leaderboard.models.constants.ApplicationConstants.SuccessResponse.GAME_SCORE_PUBLISHED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameController {

    private final GameScoreProducer gameScoreProducer;

    @PostMapping("/player-score/publish")
    public ResponseEntity<String> playerScorePublish(@RequestBody KafkaPlayerScoreEvent kafkaPlayerScoreEvent){
        gameScoreProducer.publishScore(kafkaPlayerScoreEvent);
        return ResponseEntity.status(HttpStatus.OK).body(GAME_SCORE_PUBLISHED);
    }


}
