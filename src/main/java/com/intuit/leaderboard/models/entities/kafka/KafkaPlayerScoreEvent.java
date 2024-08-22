package com.intuit.leaderboard.models.entities.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaPlayerScoreEvent {

    private String playerId;
    private String playerName;
    private int playerScore;
    private Date createdAt;
    private String createdBy;
}
