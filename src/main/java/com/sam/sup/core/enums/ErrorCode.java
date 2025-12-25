package com.sam.sup.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
  USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username already exists"),
  DISPLAY_NAME_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "Display name cannot blank"),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access Denied: You don't have permission");

  HttpStatus httpStatus;
  String message;
}
