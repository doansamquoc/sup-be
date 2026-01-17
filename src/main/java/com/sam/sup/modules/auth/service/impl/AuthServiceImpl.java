package com.sam.sup.modules.auth.service.impl;

import com.sam.sup.modules.auth.dto.internal.OAuthUserDto;
import com.sam.sup.modules.auth.dto.request.CreationRequest;
import com.sam.sup.modules.auth.dto.request.LoginRequest;
import com.sam.sup.modules.auth.dto.request.OAuthLoginRequest;
import com.sam.sup.modules.auth.dto.response.LoginResult;
import com.sam.sup.modules.auth.service.oauth.OAuthLoginStrategy;
import com.sam.sup.modules.auth.service.oauth.OAuthLoginStrategyFactory;
import com.sam.sup.modules.auth.entity.RefreshToken;
import com.sam.sup.modules.auth.mapper.AuthMapper;
import com.sam.sup.modules.auth.service.AuthService;
import com.sam.sup.modules.auth.service.RefreshTokenService;
import com.sam.sup.common.enums.ErrorCode;
import com.sam.sup.common.enums.Role;
import com.sam.sup.common.exception.BusinessException;
import com.sam.sup.common.service.JwtService;
import com.sam.sup.modules.user.dto.response.UserResponse;
import com.sam.sup.modules.user.entity.User;
import com.sam.sup.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
  AuthenticationManager authenticationManager;
  PasswordEncoder passwordEncoder;
  UserService userService;
  RefreshTokenService sessionService;
  JwtService jwtService;
  AuthMapper mapper;
  OAuthLoginStrategyFactory socialLoginStrategyFactory;

  private User authenticate(String identifier, String password) {
    try {
      Authentication auth =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(identifier, password));
      return (User) auth.getPrincipal();
    } catch (AuthenticationException exception) {
      throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
    }
  }

  @Override
  @Transactional
  public LoginResult oAuthLogin(OAuthLoginRequest request, String ip, String agent) {
    OAuthLoginStrategy strategy = socialLoginStrategyFactory.getStrategy(request.getProvider());
    OAuthUserDto socialUser = strategy.verifyToken(request.getIdToken());
    User user = userService.processSocialUser(socialUser);
    return generateLoginResult(user, ip, agent);
  }

  private LoginResult generateLoginResult(User user, String ip, String agent) {
    String accessToken = jwtService.generateAccessToken(user);
    RefreshToken session = sessionService.create(user, ip, agent);
    return LoginResult.builder().accessToken(accessToken).refreshToken(session.getToken()).build();
  }

  @Transactional
  @Override
  public LoginResult login(LoginRequest request, String ipAddress, String userAgent) {
    User user = authenticate(request.getIdentifier(), request.getPassword());
    return generateLoginResult(user, ipAddress, userAgent);
  }

  @Override
  public UserResponse signup(CreationRequest request) {
    if (userService.existByEmail(request.getEmail()))
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    if (userService.existByUsername(request.getUsername()))
      throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);

    User user = buildNewUser(request);
    return userService.save(user);
  }

  @Override
  public void logout(String refreshToken) {
    // Consider logged out successfully
    if (refreshToken == null || refreshToken.isBlank()) return;

    try {
      sessionService.revokeByToken(refreshToken);
    } catch (Exception e) {
      log.warn("Token not found or already revoked: {}", refreshToken);
    }
  }

  private User buildNewUser(CreationRequest request) {
    User user = mapper.fromCreationRequest(request);
    user.setRoles(Set.of(Role.USER));
    user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
    user.setVerified(false);
    return user;
  }
}
