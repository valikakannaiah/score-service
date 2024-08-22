package com.intuit.leaderboard.models.response;

import com.intuit.leaderboard.models.dto.LeaderBoardEntry;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LeaderBoardResponse {

    private Boolean success;
    private int code;
    private List<LeaderBoardEntry> leaderBoardResponse;
    private String message;

}
