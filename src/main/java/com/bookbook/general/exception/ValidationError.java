package com.bookbook.general.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;

public class ValidationError extends DefaultMessageSourceResolvable {

  public ValidationError(String code) {
    super(code);
  }

  public ValidationError(String code, String defaultMessage) {
    this(code, null, defaultMessage);
  }

  public ValidationError(String code, String[] arguments, String defaultMessage) {
    super(new String[]{code}, arguments, defaultMessage);
  }

}