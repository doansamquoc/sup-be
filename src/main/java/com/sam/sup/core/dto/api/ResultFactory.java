package com.sam.sup.core.dto.api;

import com.sam.sup.core.enums.ErrorCode;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResultFactory {

  private static <T> SuccessResult<T> successBuild(int status, T data, String message) {
    return SuccessResult.<T>builder()
        .data(data)
        .status(status)
        .message(message)
        .timestamp(LocalDateTime.now())
        .build();
  }

  private static ErrorResult errorBuild(ErrorCode errorCode, String path, String customMessage) {
    return ErrorResult.builder()
        .status(errorCode.getHttpStatus().value())
        .message(customMessage != null ? customMessage : errorCode.getMessage())
        .timestamp(LocalDateTime.now())
        .path(path)
        .build();
  }

  public static <T> ResponseEntity<@NonNull SuccessResult<T>> success(T data, String message) {
    SuccessResult<T> response = successBuild(HttpStatus.OK.value(), data, message);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<@NonNull SuccessResult<T>> created(T data, String message) {
    SuccessResult<T> response = successBuild(HttpStatus.CREATED.value(), data, message);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  public static ResponseEntity<@NonNull ErrorResult> error(ErrorCode errorCode, String path) {
    ErrorResult response = errorBuild(errorCode, path, null);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  public static ResponseEntity<@NonNull ErrorResult> error(
      ErrorCode errorCode, String path, String customMessage) {
    ErrorResult response = errorBuild(errorCode, path, customMessage);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }
}
