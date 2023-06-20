package com.paperized.worldscrape.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ScraperRequestFailedException extends RuntimeException {
  private final HttpStatus errorStatus;
  private final String scrapaperErrorResult;
}
