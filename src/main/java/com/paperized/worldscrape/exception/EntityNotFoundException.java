package com.paperized.worldscrape.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("Entity with those criteria not found.");
    }
}
