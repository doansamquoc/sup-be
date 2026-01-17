package com.sam.sup.common.constant;

public class AppConstant {
  public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
  public static final String LOGIN_SUCCESS = "Login successfully";
  public static final String CREATED_SUCCESS = "Created successfully";
  public static final String LOGOUT_SUCCESS = "Logout successfully";
  public static final String REFRESH_SUCCESS = "Refreshed successfully";
  public static final String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/**", "/api/v1/oauth2/**"};
  public static final String[] SWAGGER_ENDPOINTS = {
    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
  };
}
