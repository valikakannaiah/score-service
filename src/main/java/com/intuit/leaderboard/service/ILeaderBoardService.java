package com.intuit.leaderboard.service;

import com.intuit.leaderboard.models.response.LeaderBoardResponse;

public interface ILeaderBoardService {
    public LeaderBoardResponse getTopScores(int limit);
}
