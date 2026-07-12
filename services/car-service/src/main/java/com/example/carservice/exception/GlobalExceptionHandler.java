package com.example.carservice.exception;

import com.example.carservice.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException exception) {
    log.warn("Handled response status exception status={} reason={}",
        exception.getStatusCode(), messageOf(exception), exception);
    return ResponseEntity.status(exception.getStatusCode())
        .body(ApiResponse.error(messageOf(exception)));
  }

  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception exception) {
    log.warn("Invalid request", exception);
    return ResponseEntity.status(BAD_REQUEST).body(ApiResponse.error("Invalid request"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException exception) {
    log.warn("Access denied", exception);
    return ResponseEntity.status(FORBIDDEN).body(ApiResponse.error("Access denied"));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException exception) {
    log.warn("Data constraint violation", exception);
    return ResponseEntity.status(CONFLICT).body(ApiResponse.error("Data constraint violation"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception exception) {
    log.error("Unhandled exception", exception);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal server error"));
  }

  private String messageOf(ResponseStatusException exception) {
    return exception.getReason() != null ? exception.getReason() : exception.getStatusCode().toString();
  }
}
