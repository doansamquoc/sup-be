package com.sam.sup.auth.controller;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.auth.dto.request.LoginRequest;
import com.sam.sup.auth.dto.response.AuthResponse;
import com.sam.sup.auth.dto.response.LoginResult;
import com.sam.sup.auth.service.AuthService;
import com.sam.sup.core.config.AppConstant;
import com.sam.sup.core.dto.api.ResultFactory;
import com.sam.sup.core.dto.api.SuccessResult;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.core.service.HttpCookieService;
import com.sam.sup.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;
  HttpCookieService cookieService;

  @PostMapping("/login")
  public ResponseEntity<@NonNull SuccessResult<AuthResponse>> login(
      @RequestBody @Valid LoginRequest request, HttpServletRequest servletRequest) {

    String userAgent = Util.getUserAgent(servletRequest);
    String ipAddress = Util.getIpAddress(servletRequest);
    LoginResult result = authService.login(request, ipAddress, userAgent);

    ResponseCookie sessionToken = cookieService.createRefreshToken(result.getRefreshToken());
    AuthResponse response = new AuthResponse(result.getAccessToken());

    return ResultFactory.success(sessionToken.toString(), response, AppConstant.LOGIN_SUCCESS);
  }

  @PostMapping("/signup")
  public ResponseEntity<@NonNull SuccessResult<UserResponse>> signup(
      @RequestBody @Valid CreationRequest request) {
    UserResponse response = authService.signup(request);
    return ResultFactory.created(response, AppConstant.CREATED_SUCCESS);
  }

  @PostMapping("/logout")
  public ResponseEntity<@NonNull SuccessResult<Void>> logout(
      HttpServletRequest servletRequest,
      @CookieValue(name = AppConstant.REFRESH_TOKEN_COOKIE_NAME, required = false)
          String refreshToken) {

    if (refreshToken == null || refreshToken.isBlank()) {
      refreshToken = cookieService.extractRefreshTokenFromServletRequest(servletRequest);
    }

    // Logout processing
    authService.logout(refreshToken);

    // Clear session in cookie
    ResponseCookie cleanCookie = cookieService.deleteRefreshToken();

    // Clear context
    SecurityContextHolder.clearContext();

    return ResultFactory.success(cleanCookie.toString(), null, AppConstant.LOGOUT_SUCCESS);
  }
}
