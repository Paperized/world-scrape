package com.paperized.worldscrape.exception;

public class ActionNotPermittedException extends RuntimeException {
  public ActionNotPermittedException() {
    this("You are not allowed to perform this action");
  }
  public ActionNotPermittedException(String message) {
    super(message);
  }
}
