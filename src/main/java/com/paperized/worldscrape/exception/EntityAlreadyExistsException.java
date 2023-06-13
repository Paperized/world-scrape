package com.paperized.worldscrape.exception;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException {
  private final String entityName;
  public EntityAlreadyExistsException(String entityName) {
    super(entityName + " already exists.");
    this.entityName = entityName;
  }
}
