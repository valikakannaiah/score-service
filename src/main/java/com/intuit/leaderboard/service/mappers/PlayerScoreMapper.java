package com.intuit.leaderboard.service.mappers;

import com.intuit.leaderboard.models.dto.LeaderBoardEntry;
import com.intuit.leaderboard.models.entities.PlayerScore;
import com.intuit.leaderboard.models.dto.RedisScore;
import com.intuit.leaderboard.models.entities.kafka.KafkaPlayerScoreEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerScoreMapper {

    public PlayerScore mapToEntity(KafkaPlayerScoreEvent event){
        PlayerScore score = new PlayerScore();
        score.setPlayerId(event.getPlayerId());
        score.setPlayerScore(event.getPlayerScore());
        score.setPlayerName(event.getPlayerName());
        score.setCreatedAt(event.getCreatedAt());
        score.setCreatedBy(event.getCreatedBy());

        return score;
    }

    public LeaderBoardEntry maptToLeaderBoard(RedisScore playerScore, int score, int rank){
        LeaderBoardEntry leaderBoardEntry = new LeaderBoardEntry();
        leaderBoardEntry.setPlayerId(playerScore.getPlayerId());
        leaderBoardEntry.setPlayerName(playerScore.getPlayerName());
        leaderBoardEntry.setScore(score);
        leaderBoardEntry.setRank(rank);
        return leaderBoardEntry;
    }

    public List<LeaderBoardEntry> maptToLeaderBoardResponse(List<PlayerScore> playerScores){
        List<LeaderBoardEntry> scores = new ArrayList<>();
        int rank = 0;
        for(PlayerScore playerScore: playerScores){
            LeaderBoardEntry leaderBoardEntry = new LeaderBoardEntry();
            leaderBoardEntry.setPlayerId(playerScore.getPlayerId());
            leaderBoardEntry.setPlayerName(playerScore.getPlayerName());
            leaderBoardEntry.setScore(playerScore.getPlayerScore());
            leaderBoardEntry.setRank(++rank);
            scores.add(leaderBoardEntry);
        }

        return scores;
    }

    public RedisScore maptToRedisScore(PlayerScore playerScore){
        RedisScore redisScore = new RedisScore();
        redisScore.setPlayerId(playerScore.getPlayerId());
        redisScore.setPlayerName(playerScore.getPlayerName());
        return redisScore;
    }

}
