package com.sam.sup.core.exception;

import com.sam.sup.core.dto.ApiResponse;
import com.sam.sup.core.dto.ApiResponseFactory;
import com.sam.sup.core.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusiness(
            BusinessException exception,
            HttpServletRequest servletRequest
    ) {
        ErrorCode errorCode = exception.getErrorCode();
        return ApiResponseFactory.error(errorCode, servletRequest.getServletPath());
    }

    @SuppressWarnings("NullableProblems")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest servletRequest
    ) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        return ApiResponseFactory.error(errorCode, servletRequest.getServletPath());
    }

    @SuppressWarnings("NullableProblems")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(
            Exception exception,
            HttpServletRequest servletRequest
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        String message = exception.getMessage();
        return ApiResponseFactory.error(errorCode, servletRequest.getServletPath(), message);
    }
}
