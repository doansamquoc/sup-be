package com.sam.sup.core.dto;

import com.sam.sup.core.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SuppressWarnings("NullableProblems")
public class ApiResponseFactory {
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()

                .status(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode, String path) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.name())
                .message(errorCode.getMessage())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode, String path, String customMessage) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.name())
                .message(customMessage)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }
}

