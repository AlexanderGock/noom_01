package com.noom.interview.fullstack.sleep.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.noom.interview.fullstack.sleep.exception.RecordAlreadyExistsException;
import com.noom.interview.fullstack.sleep.exception.SleepNotFoundException;
import com.noom.interview.fullstack.sleep.rest.response.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestErrorHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception e, HttpServletRequest request) {
    log.error("Internal error", e);
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(e.getMessage())
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
    log.error(e.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(getErrorMessage(e).orElse(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
    log.error(e.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(Optional.ofNullable(e.getMessage()).orElse(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageConversionException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageConversionException e, HttpServletRequest request) {
    log.error(e.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(getErrorMessage(e).orElse(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RecordAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleRecordAlreadyExistsException(RecordAlreadyExistsException e, HttpServletRequest request) {
    log.error(e.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getMessage())
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SleepNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleSleepNotFoundException(SleepNotFoundException e, HttpServletRequest request) {
    log.error(e.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.NOT_FOUND.value())
        .error(e.getMessage())
        .build();
    return new ResponseEntity<>(errorResponse, getResponseHeaders(), HttpStatus.NOT_FOUND);
  }

  private MultiValueMap<String, String> getResponseHeaders() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    return headers;
  }

  private Optional<String> getErrorMessage(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    if (!errors.isEmpty()) {
      return Optional.of(String.join(", ", errors));
    }
    return Optional.empty();
  }

  private Optional<String> getErrorMessage(HttpMessageConversionException ex) {
    List<String> messageParts = new ArrayList<>();
    if (ex.getCause() instanceof JsonMappingException) {
      JsonMappingException jme = (JsonMappingException)ex.getCause();
      if (!CollectionUtils.isEmpty(jme.getPath())) {
        messageParts.add(
            jme.getPath().stream().map(Reference::getFieldName).collect(Collectors.joining(".")));
      }
    }
    if (ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException)ex.getCause();
      if (ife.getValue() != null) {
        messageParts.add(ife.getValue().toString());
      }
    }
    if (!messageParts.isEmpty()) {
      return Optional.of(StringUtils.join(messageParts, ": "));
    }
    return Optional.empty();
  }
}
