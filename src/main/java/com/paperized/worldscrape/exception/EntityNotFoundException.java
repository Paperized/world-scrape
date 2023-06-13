package com.paperized.worldscrape.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
  private final String entityName;
  public EntityNotFoundException(String entityName) {
    super(entityName + " not found.");
    this.entityName = entityName;
  }
}
