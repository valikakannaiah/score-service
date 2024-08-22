package com.intuit.leaderboard.service;

import com.intuit.leaderboard.exceptions.LeaderBoardCustomException;
import com.intuit.leaderboard.models.constants.ErrorStatusCode;
import com.intuit.leaderboard.models.dto.LeaderBoardEntry;
import com.intuit.leaderboard.models.entities.PlayerScore;
import com.intuit.leaderboard.models.response.LeaderBoardResponse;
import com.intuit.leaderboard.repository.PlayerScoreRepository;
import com.intuit.leaderboard.service.mappers.PlayerScoreMapper;
import com.intuit.leaderboard.service.utils.CacheUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LeaderBoardServiceTest {

    @Mock
    private CacheUtil cacheUtil;

    @Mock
    private PlayerScoreRepository playerScoreRepository;

    @Mock
    private PlayerScoreMapper playerScoreMapper;

//    @InjectMocks
//    private LeaderBoardService leaderBoardService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);


    }


    @Test
    public void getTopScoresTest_exceedRanks() {
        int limit = 30;

        LeaderBoardCustomException exception = assertThrows(LeaderBoardCustomException.class, () -> {
            new LeaderBoardService(10, cacheUtil, playerScoreRepository, playerScoreMapper).getTopScores(limit);        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertTrue(exception.getMessages().contains(ErrorStatusCode.TOP_SCORE_LIMIT_EXCEEDED.getValue()));
    }

    @Test
    public void getTopScoresFetchFromCache() {
        int limit = 5;
        LeaderBoardResponse cachedResponse = new LeaderBoardResponse();
        when(cacheUtil.fetchFromCache(limit)).thenReturn(cachedResponse);
        LeaderBoardResponse response = new LeaderBoardService(10, cacheUtil, playerScoreRepository, playerScoreMapper).getTopScores(limit);
        verify(cacheUtil, times(1)).fetchFromCache(limit);
        verify(playerScoreRepository, never()).findTopScores(anyInt());
        assertEquals(cachedResponse, response);
    }


    @Test
    public void getTopScores_fetchFromDB() {
        int limit = 5;

        ArrayList<PlayerScore> playerScoreList = new ArrayList<>();
        when(playerScoreRepository.findTopScores(anyInt())).thenReturn(playerScoreList);

        LeaderBoardService leaderBoardService = new LeaderBoardService(10, cacheUtil, playerScoreRepository, playerScoreMapper);

        LeaderBoardResponse response = leaderBoardService.fetchFromDB(limit);
        verify(playerScoreRepository).findTopScores(eq(5));
        verify(playerScoreRepository, times(1)).findTopScores(limit);
        assertNotNull(response);
        assertNotNull(response.getLeaderBoardResponse());
        assertTrue(response.getLeaderBoardResponse().isEmpty());
    }

    @Test
    public void getTopScores_fetchFromDBWhenException() {
        int limit = 5;

        ArrayList<PlayerScore> playerScoreList = new ArrayList<>();
        when(playerScoreRepository.findTopScores(anyInt())).thenReturn(playerScoreList);

        LeaderBoardService leaderBoardService = new LeaderBoardService(10, cacheUtil, playerScoreRepository, playerScoreMapper);

        LeaderBoardResponse response = leaderBoardService.fetchFromDB(limit);
        verify(playerScoreRepository).findTopScores(eq(5));
        verify(playerScoreRepository, times(1)).findTopScores(limit);
        assertNotNull(response);
        assertNotNull(response.getLeaderBoardResponse());
        assertTrue(response.getLeaderBoardResponse().isEmpty());
    }

}
