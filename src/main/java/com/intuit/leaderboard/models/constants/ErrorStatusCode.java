package com.intuit.leaderboard.models.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatusCode {
    BAD_REQUEST("bad_request", "400"),
    NOT_FOUND("resource not found", "404"),
    TOP_SCORE_LIMIT_EXCEEDED("Limit is greater than max ranks", "400"),
    AUTHENTICATION_ERROR("Not authorized", "400");
    private final String value;
    private final String httpStatusCode;

}
