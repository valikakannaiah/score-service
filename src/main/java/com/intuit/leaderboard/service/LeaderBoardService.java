package com.intuit.leaderboard.service;

import com.intuit.leaderboard.exceptions.LeaderBoardCustomException;
import com.intuit.leaderboard.models.dto.LeaderBoardEntry;
import com.intuit.leaderboard.models.entities.PlayerScore;
import com.intuit.leaderboard.models.constants.ErrorStatusCode;
import com.intuit.leaderboard.models.response.LeaderBoardResponse;
import com.intuit.leaderboard.repository.PlayerScoreRepository;
import com.intuit.leaderboard.service.mappers.PlayerScoreMapper;
import com.intuit.leaderboard.service.utils.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderBoardService implements ILeaderBoardService{
    @Value("${spring.redis.maxNoOfRanks}")
    private int maxRanks;

    private final CacheUtil cacheUtil;

    private final PlayerScoreRepository playerScoreRepository;

    private final PlayerScoreMapper playerScoreMapper;

    public LeaderBoardResponse getTopScores(int limit) {
        LeaderBoardResponse response = null;

        if (limit > maxRanks) {
            log.info("Limit is greater than max ranks");
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(ErrorStatusCode.TOP_SCORE_LIMIT_EXCEEDED.getValue()));
        }
        try {
            response = cacheUtil.fetchFromCache(limit);
            if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getLeaderBoardResponse())) {
                fetchFromDB(limit);
            }
        } catch (LeaderBoardCustomException exp) {
            log.error("Error fetching scores from cache. Fetching details from DB");
            fetchFromDB(limit);
        } catch(Exception e){
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
        }
        return response;
    }

    public LeaderBoardResponse fetchFromDB(int limit) {
        LeaderBoardResponse response = new LeaderBoardResponse();
        Optional<List<PlayerScore>> topScoresFromDB = Optional.ofNullable(playerScoreRepository.findTopScores(limit));
        if (topScoresFromDB.isPresent()) {
            List<LeaderBoardEntry> leaderBoardScores = playerScoreMapper.maptToLeaderBoardResponse(topScoresFromDB.get());
            response.setLeaderBoardResponse(leaderBoardScores);
        }else{
            log.info("No scores found in DB");
            response.setLeaderBoardResponse(Collections.emptyList());
        }
        return response;
    }
}
