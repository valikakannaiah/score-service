package com.intuit.leaderboard.exceptions;

import com.intuit.leaderboard.models.response.LeaderBoardResponse;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(LeaderBoardCustomException.class)
    public ResponseEntity<LeaderBoardResponse> handleCustomException(LeaderBoardCustomException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        LeaderBoardResponse leaderBoardResponse = new LeaderBoardResponse();
        leaderBoardResponse.setSuccess(Boolean.FALSE);
        leaderBoardResponse.setCode(400);
        leaderBoardResponse.setMessage(ex.getMessages().get(0));

        return new ResponseEntity<>(leaderBoardResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LeaderBoardResponse> handleException(Exception ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        LeaderBoardResponse leaderBoardResponse = new LeaderBoardResponse();
        leaderBoardResponse.setSuccess(Boolean.FALSE);
        leaderBoardResponse.setCode(500);
        leaderBoardResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(leaderBoardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
