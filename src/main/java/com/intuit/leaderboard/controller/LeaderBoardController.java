package com.intuit.leaderboard.controller;

import com.intuit.leaderboard.models.constants.ApplicationConstants;
import com.intuit.leaderboard.models.response.LeaderBoardResponse;
import com.intuit.leaderboard.service.LeaderBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.intuit.leaderboard.models.constants.ApplicationConstants.Params.DEFAULT_LIMIT;
import static com.intuit.leaderboard.models.constants.ApplicationConstants.Params.LIMIT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    @GetMapping("/topScores")
    public ResponseEntity<LeaderBoardResponse> getTopScores(
            @RequestParam(value = LIMIT, defaultValue = DEFAULT_LIMIT, required = false) int limit
    ){
        LeaderBoardResponse response = leaderBoardService.getTopScores(limit);
        response.setCode(200);
        response.setMessage(ApplicationConstants.SuccessResponse.SCORES_FETCHED);
        response.setSuccess(true);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
