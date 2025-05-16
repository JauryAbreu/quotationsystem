package com.quotationsystem.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Map<String, String>> handleNullPointerException(
      NullPointerException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer error: " + ex.getMessage());
  }

  /*@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEntityNotFoundException(
      javax.persistence.EntityNotFoundException ex, WebRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }*/

  @ExceptionHandler(org.springframework.dao.DataAccessException.class)
  public ResponseEntity<Map<String, String>> handleDataAccessException(
      org.springframework.dao.DataAccessException ex, WebRequest request) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Database error: " + ex.getMessage());
  }

  @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex, WebRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
  }

  private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String message) {
    Map<String, String> response = new HashMap<>();
    response.put("error", message);
    response.put("status", status.toString());
    return new ResponseEntity<>(response, status);
  }
}
