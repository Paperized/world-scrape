package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.exception.ApiErrorResponse;
import com.paperized.worldscrape.exception.EntityAlreadyExistsException;
import com.paperized.worldscrape.exception.EntityNotFoundException;
import com.paperized.worldscrape.exception.ScraperRequestFailedException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private final Map<String, String> constrainsErrorCodeMap;

  public GlobalExceptionHandler(@Qualifier("constraintsTranslator") Map<String, String> constrainsErrorCodeMap) {
    this.constrainsErrorCodeMap = constrainsErrorCodeMap;
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> entityNotFoundExceptionHandling(Exception exception) {
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(HttpStatus.NOT_FOUND,
        "entityNotFound",
        exception.getMessage()),
      HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> entityAlreadyExistsExceptionHandling(Exception exception) {
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(HttpStatus.CONFLICT,
        "entityAlreadyExists",
        exception.getMessage()),
      HttpStatus.CONFLICT
    );
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> dataIntegrityViolationExceptionHandling(DataIntegrityViolationException exception) {
    if(!(exception.getCause() instanceof ConstraintViolationException violationException)) {
      return internalErrorException(exception);
    }

    String errorCode = constrainsErrorCodeMap.getOrDefault(violationException.getConstraintName(), "uc_unhandled");
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(HttpStatus.CONFLICT,
        errorCode,
        "Field already in use"),
      HttpStatus.CONFLICT
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> accessDeniedExceptionHandling(Exception exception) {
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(HttpStatus.UNAUTHORIZED,
        "accessDenied",
        "Please log in"),
      HttpStatus.UNAUTHORIZED
    );
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiErrorResponse> authenticationExceptionHandling(Exception exception) {
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    String errorCode, errorMessage;
    if (exception instanceof BadCredentialsException) {
      httpStatus = HttpStatus.BAD_REQUEST;
      errorCode = "badCredentials";
      errorMessage = "Invalid username or password";
    } else if (exception instanceof LockedException) {
      errorCode = "lockedAccount";
      errorMessage = "User account is locked";
    } else if (exception instanceof DisabledException) {
      errorCode = "disabledAccount";
      errorMessage = "User account is disabled";
    } else {
      return internalErrorException(exception);
    }

    return new ResponseEntity<>(ApiErrorResponse.fromErrors(httpStatus, errorCode, errorMessage), httpStatus);
  }

  @ExceptionHandler(ScraperRequestFailedException.class)
  public ResponseEntity<ApiErrorResponse> scraperRequestExceptionException(ScraperRequestFailedException exception) {
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(exception.getErrorStatus(),
        "scraperError",
        exception.getErrorMessage()),
      exception.getErrorStatus()
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> internalErrorException(Exception exception) {
    exception.printStackTrace();
    return new ResponseEntity<>(
      ApiErrorResponse.fromErrors(HttpStatus.INTERNAL_SERVER_ERROR,
        "internalError",
        "An unexpected error occurred"),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
