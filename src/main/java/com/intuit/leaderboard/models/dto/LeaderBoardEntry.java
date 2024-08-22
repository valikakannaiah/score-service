package com.intuit.leaderboard.models.dto;

import lombok.Data;

@Data
public class LeaderBoardEntry {
    private String playerId;
    private String playerName;
    private int Score;
    private int rank;
}
