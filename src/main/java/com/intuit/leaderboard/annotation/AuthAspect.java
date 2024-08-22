package com.intuit.leaderboard.annotation;

import com.intuit.leaderboard.exceptions.LeaderBoardCustomException;
import com.intuit.leaderboard.models.constants.ApplicationConstants;
import com.intuit.leaderboard.models.constants.ErrorStatusCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Aspect
@Component
@Slf4j
public class AuthAspect {
  @Value("${leaderboard.isc-key}")
  private String authKey;

  @Before("within(com.intuit.leaderboard.controller..* )")
  public void preHandle(JoinPoint joinPoint) throws LeaderBoardCustomException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String iscKey = request.getHeader(ApplicationConstants.API_KEY);
    if (ObjectUtils.isEmpty(iscKey) || !iscKey.equalsIgnoreCase(authKey)) {
      log.error("API-KEY Authorization failure: key {}", iscKey);
      throw new LeaderBoardCustomException(
              HttpStatus.UNAUTHORIZED,
              List.of(ErrorStatusCode.AUTHENTICATION_ERROR.getValue())
      );
    }
    log.info("API-KEY Authorization successful");
  }
}
