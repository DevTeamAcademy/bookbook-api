package com.bookbook.general.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
@Order()
public class GlobalExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @Autowired
  private MessageSource messageSource;

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ErrorResponse notValidException(MethodArgumentNotValidException e) {
    BindingResult result = e.getBindingResult();
    LOGGER.info(e.getMessage());
    ErrorResponse error = new ErrorResponse("Validation Error");
    processFieldErrors(error, result.getFieldErrors());
    processErrors(error, result.getGlobalErrors());
    return error;
  }

  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseBody
  public ResponseEntity<String> clientErrorException(HttpClientErrorException e) {
    LOGGER.error(e.getMessage(), e);
    return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  @ResponseBody
  @ResponseStatus(BAD_REQUEST)
  public ErrorResponse errorException(OptimisticLockingFailureException e) {
    LOGGER.warn(e.getMessage(), e);
    return new ErrorResponse(messageSource.getMessage("error.optimisticLocking", null,
        "Data was updated by other user.", Locale.getDefault()));
  }

  private void processFieldErrors(ErrorResponse error, List<FieldError> fieldErrors) {
    for (FieldError fieldError : fieldErrors) {
      error.addInvalidField(fieldError.getField(), messageSource.getMessage(fieldError, Locale.getDefault()));
    }
  }

  private void processErrors(ErrorResponse error, List<ObjectError> objectErrors) {
    for (ObjectError objectError : objectErrors) {
      error.addObjectError(messageSource.getMessage(objectError, Locale.getDefault()));
    }
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ResponseBody
  @ExceptionHandler(NotFoundException.class)
  public ErrorResponse notFoundExceptionHandler(NotFoundException e) {
    LOGGER.error("Not found : ", e);
    return new ErrorResponse(e.getMessage());
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ResponseBody
  @ExceptionHandler(InvalidParameterException.class)
  public ErrorResponse invalidRequestExceptionHandler(InvalidParameterException e) {
    LOGGER.error(e.getMessage(), e);
    return new ErrorResponse(e.getMessage());
  }

  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(ValidationException.class)
  public ErrorResponse amousValidationExceptionHandler(ValidationException e) {
    LOGGER.info(messageSource.getMessage(e.getErrorMessage(), Locale.getDefault()));
    return new ErrorResponse(messageSource.getMessage(e.getErrorMessage(), Locale.getDefault()));
  }

//  private List<String> getErrorsLocalization(List<AmousValidationError> errorsMassageList) {
//    return errorsMassageList.stream().map(error -> messageSource.getMessage(error, Locale.getDefault())).collect(Collectors.toList());
//  }

  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponse onHttpMessageNotReadable(final HttpMessageNotReadableException e, WebRequest request) {
    final Throwable cause = e.getCause();
    LOGGER.error("Can not parse request: " + request, e);
    if (cause instanceof InvalidFormatException) {
      InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
      String fieldName = invalidFormatException.getPath().get(invalidFormatException.getPath().size() - 1).getFieldName();
      String value = invalidFormatException.getValue().toString();
      String defErrorMsg = String.format("Incorrect value %s for field %s.", value, fieldName);
      return new ErrorResponse(messageSource.getMessage("error.json.value", new String[]{value, fieldName}, defErrorMsg, Locale.getDefault()));
    }
    throw e;
  }

}