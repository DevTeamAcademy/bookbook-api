package com.bookbook.general.exception;

public class ValidationException extends RuntimeException {

  private ValidationError errorMessage;

  public ValidationException(String code, String defaultMessage) {
    this(new ValidationError(code, defaultMessage));
  }

  public ValidationException(String code, String[] arguments, String defaultMessage) {
    this(new ValidationError(code, arguments, defaultMessage));
  }

  public ValidationException(ValidationError errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ValidationError getErrorMessage() {
    return errorMessage;
  }

}