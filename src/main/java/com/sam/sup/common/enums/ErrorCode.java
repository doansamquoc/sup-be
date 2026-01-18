package com.sam.sup.common.enums;

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
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid Credentials"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token"),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email Already Exists"),
  USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username Already Exists"),
  DISPLAY_NAME_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "Display Name Cannot Blank"),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access Denied: You Don't Have Permission"),
  ID_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "ID Token Is Null"),
  UNVERIFIED_EMAIL(HttpStatus.NOT_ACCEPTABLE, "Unverified Email"),
  PROVIDER_NOT_SUPPORTED(HttpStatus.NOT_ACCEPTABLE, "OAuth Login Provider Is Not Supported"),
  INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "Invalid OAuth Token"),
  LOGIN_PROVIDER_CANNOT_NULL(HttpStatus.BAD_REQUEST, "Login Provider Cannot Null"),
  TOKEN_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "ID Token Cannot Blank"),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Request"),
  TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "Token Has Been Revoked"),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token Has Expired"),
  REFRESH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "Refresh Token Is Missing");
  HttpStatus httpStatus;
  String message;
}
