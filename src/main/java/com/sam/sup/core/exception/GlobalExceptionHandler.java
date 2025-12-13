package com.sam.sup.core.exception;

import com.sam.sup.core.dto.api.ErrorResult;
import com.sam.sup.core.dto.api.ResultFactory;
import com.sam.sup.core.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<@NonNull ErrorResult> handleBusiness(
            BusinessException exception,
            HttpServletRequest servletRequest
    ) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResultFactory.error(errorCode, servletRequest.getServletPath());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResult> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest servletRequest
    ) {
        FieldError fieldError = exception.getFieldError();
        if (fieldError == null || fieldError.getDefaultMessage() == null) {
            throw new IllegalStateException("Validation error without field message");
        }
        ErrorCode errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());
        return ResultFactory.error(errorCode, servletRequest.getServletPath());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResult> handleGeneric(
            Exception exception,
            HttpServletRequest servletRequest
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            message = errorCode.getMessage();
        }
        return ResultFactory.error(errorCode, servletRequest.getServletPath(), message);
    }
}
