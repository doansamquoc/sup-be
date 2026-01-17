package com.sam.sup.common.service;

import com.sam.sup.common.constant.AppConstant;
import com.sam.sup.common.config.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HttpCookieService {
  AppProperties appProperties;

  // Extract cookie each request to server
  public String extractRefreshTokenFromServletRequest(HttpServletRequest servletRequest) {
    if (servletRequest.getCookies() != null)
      for (Cookie cookie : servletRequest.getCookies()) {
        if (AppConstant.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    return null;
  }

  private ResponseCookie build(String name, String value, long maxAge, String path) {
    return ResponseCookie.from(name, value)
        .httpOnly(true)
        .secure(false)
        .path(path)
        .sameSite("Lax")
        .maxAge(maxAge)
        .build();
  }

  public ResponseCookie create(String name, String value, long maxAge, String path) {
    return build(name, value, maxAge, path);
  }

  public ResponseCookie createRefreshToken(String value) {
    return create(
        AppConstant.REFRESH_TOKEN_COOKIE_NAME,
        value,
        appProperties.getRefreshTokenExpiration() / 1000,
        "/");
  }

  public ResponseCookie deleteRefreshToken() {
    return delete(AppConstant.REFRESH_TOKEN_COOKIE_NAME, "/");
  }

  public ResponseCookie delete(String name, String path) {
    return build(name, null, 0, path);
  }

  public void addCookie(HttpServletResponse servletResponse, ResponseCookie cookie) {
    servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public Optional<Cookie> getCookie(HttpServletRequest servletRequest, String name) {
    if (servletRequest.getCookies() == null) return Optional.empty();
    return Arrays.stream(servletRequest.getCookies())
        .filter(cookie -> cookie.getName().equals(name))
        .findFirst();
  }
}
