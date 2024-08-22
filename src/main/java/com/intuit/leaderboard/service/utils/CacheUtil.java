package com.intuit.leaderboard.service.utils;

import com.intuit.leaderboard.exceptions.LeaderBoardCustomException;
import com.intuit.leaderboard.models.dto.LeaderBoardEntry;
import com.intuit.leaderboard.models.constants.ErrorStatusCode;
import com.intuit.leaderboard.models.dto.RedisScore;
import com.intuit.leaderboard.models.response.LeaderBoardResponse;
import com.intuit.leaderboard.service.mappers.PlayerScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheUtil {

    @Value("${spring.redis.key}")
    private String LEADERBOARD_KEY;
    @Value("${spring.redis.maxNoOfRanks}")
    private int maxRanks;
    private final RedisTemplate<String, RedisScore> redisTemplate;

    private final PlayerScoreMapper playerScoreMapper;

    public void addToCache(RedisScore playerScore, int score){
        try {
            ZSetOperations<String, RedisScore> zSetOps = redisTemplate.opsForZSet();
            zSetOps.add(LEADERBOARD_KEY, playerScore, score);
            redisTemplate.opsForZSet().removeRange(LEADERBOARD_KEY, maxRanks, -1);
        }catch(Exception e){
            log.error("Error setting the key: {} with value: {} for hashKey: {}, exception: {}",playerScore, score,
                    LEADERBOARD_KEY, Arrays.toString(e.getStackTrace()));
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
        }
    }

    public LeaderBoardResponse fetchFromCache(int limit){
        LeaderBoardResponse response = new LeaderBoardResponse();
        List<LeaderBoardEntry> scores = new ArrayList<>();
        try{
            Set<ZSetOperations.TypedTuple<RedisScore>> topPlayers = redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, limit-1);

            if(ObjectUtils.isNotEmpty(topPlayers)){
                for (ZSetOperations.TypedTuple<RedisScore> playerScore : topPlayers) {
                    RedisScore player = playerScore.getValue();
                    Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, playerScore.getValue());
                    Double score = redisTemplate.opsForZSet().score(LEADERBOARD_KEY, playerScore.getValue());
                    scores.add(playerScoreMapper.maptToLeaderBoard(player, score.intValue(), rank.intValue() + 1));
                }
                response.setLeaderBoardResponse(scores);
            }
        }catch (Exception e){
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(ErrorStatusCode.BAD_REQUEST.getValue(), e.getMessage()));
        }
        return response;
    }
}
