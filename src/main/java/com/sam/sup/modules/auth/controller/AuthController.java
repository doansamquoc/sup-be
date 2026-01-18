package com.sam.sup.modules.auth.controller;

import com.sam.sup.modules.auth.dto.request.CreationRequest;
import com.sam.sup.modules.auth.dto.request.LoginRequest;
import com.sam.sup.modules.auth.dto.request.OAuthLoginRequest;
import com.sam.sup.modules.auth.dto.response.AuthResponse;
import com.sam.sup.modules.auth.dto.response.LoginResult;
import com.sam.sup.modules.auth.service.AuthService;
import com.sam.sup.common.annotation.ClientIp;
import com.sam.sup.common.annotation.UserAgent;
import com.sam.sup.common.constant.AppConstant;
import com.sam.sup.common.api.ResultFactory;
import com.sam.sup.common.api.SuccessResult;
import com.sam.sup.modules.user.dto.response.UserResponse;
import com.sam.sup.common.service.HttpCookieService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;
  HttpCookieService cookieService;

  private ResponseEntity<SuccessResult<AuthResponse>> handleLoginSuccess(LoginResult result) {
    ResponseCookie sessionCookie = cookieService.createRefreshToken(result.getRefreshToken());
    AuthResponse response = new AuthResponse(result.getAccessToken());
    return ResultFactory.success(sessionCookie.toString(), response, AppConstant.LOGIN_SUCCESS);
  }

  @PostMapping("/refresh")
  public ResponseEntity<@NonNull SuccessResult<AuthResponse>> refresh(
      @CookieValue(name = "refreshToken", required = false) String refreshTokenString,
      @ClientIp String ip,
      @UserAgent String agent) {
    LoginResult result = authService.refresh(refreshTokenString, ip, agent);
    return handleLoginSuccess(result);
  }

  @PostMapping("/google")
  public ResponseEntity<@NonNull SuccessResult<AuthResponse>> google(
      @RequestBody @Valid OAuthLoginRequest request, @ClientIp String ip, @UserAgent String agent) {

    LoginResult result = authService.oAuthLogin(request, ip, agent);
    return handleLoginSuccess(result);
  }

  @PostMapping("/login")
  public ResponseEntity<@NonNull SuccessResult<AuthResponse>> login(
      @RequestBody @Valid LoginRequest request, @ClientIp String ip, @UserAgent String agent) {

    LoginResult result = authService.login(request, ip, agent);
    return handleLoginSuccess(result);
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
