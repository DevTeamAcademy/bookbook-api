package com.bookbook.general.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
  private final String message;
  private List<String> errors;
  private List<InvalidField> invalidFields = new ArrayList<>();

  public ErrorResponse(String message) {
    this.message = message;
  }

  public ErrorResponse(String message, List<String> errors) {
    this(message);
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void addInvalidField(String fieldName, String message) {
    InvalidField invalidField = new InvalidField(fieldName, message);
    invalidFields.add(invalidField);
  }

  public void addObjectError(String error) {
    if (errors == null) {
      errors = new ArrayList<>();
    }
    errors.add(error);
  }

  public String getMessage() {
    return message;
  }

  public List<InvalidField> getInvalidFields() {
    return invalidFields;
  }

  public static class InvalidField {
    private final String fieldName;
    private final String message;

    public InvalidField(String fieldName, String message) {
      this.fieldName = fieldName;
      this.message = message;
    }

    public String getFieldName() {
      return fieldName;
    }

    public String getMessage() {
      return message;
    }
  }
}