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
import com.sam.sup.utils.CookieUtil;
import com.sam.sup.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<@NonNull SuccessResult<AuthResponse>> login(
      @RequestBody @Valid LoginRequest request,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    String userAgent = Util.getUserAgent(servletRequest);
    String ipAddress = Util.getIpAddress(servletRequest);
    LoginResult result = authService.login(request, ipAddress, userAgent);

    ResponseCookie sessionToken = CookieUtil.createRefreshToken(result.getRefreshToken());
    CookieUtil.addCookie(servletResponse, sessionToken);

    AuthResponse response = new AuthResponse(result.getAccessToken());

    return ResultFactory.success(response, AppConstant.LOGIN_SUCCESS);
  }

  @PostMapping("/signup")
  public ResponseEntity<@NonNull SuccessResult<UserResponse>> signup(
      @RequestBody @Valid CreationRequest request) {
    UserResponse response = authService.signup(request);
    return ResultFactory.created(response, AppConstant.CREATED_SUCCESS);
  }
}
