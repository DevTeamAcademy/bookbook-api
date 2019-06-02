package com.bookbook.general.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String entityName, String guid) {
    super("Could not find " + entityName + ", by: " + guid);
  }

  public NotFoundException(String message) {
    super(message);
  }
}

