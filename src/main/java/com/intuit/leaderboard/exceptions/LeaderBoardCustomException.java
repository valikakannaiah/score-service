package com.intuit.leaderboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
public class LeaderBoardCustomException extends RuntimeException{
    private HttpStatus httpStatus;
    private List<String> messages;
}
