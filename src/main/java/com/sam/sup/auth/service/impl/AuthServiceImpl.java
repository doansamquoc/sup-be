package com.sam.sup.auth.service.impl;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.auth.dto.request.LoginRequest;
import com.sam.sup.auth.dto.response.LoginResult;
import com.sam.sup.session.entity.Session;
import com.sam.sup.auth.mapper.AuthMapper;
import com.sam.sup.auth.service.AuthService;
import com.sam.sup.session.service.SessionService;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.enums.Role;
import com.sam.sup.core.exception.BusinessException;
import com.sam.sup.core.service.JwtService;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.user.entity.User;
import com.sam.sup.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AuthServiceImpl implements AuthService {
  AuthenticationManager authenticationManager;
  PasswordEncoder passwordEncoder;
  UserService userService;
  SessionService sessionService;
  JwtService jwtService;
  AuthMapper mapper;

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

  @Transactional
  @Override
  public LoginResult login(LoginRequest request, String ipAddress, String userAgent) {
    User user = authenticate(request.getIdentifier(), request.getPassword());
    String accessToken = jwtService.generateAccessToken(user);
    Session session = sessionService.create(user, ipAddress, userAgent);
    return LoginResult.builder().accessToken(accessToken).refreshToken(session.getToken()).build();
  }

  @Override
  public UserResponse signup(CreationRequest request) {
    if (userService.existByEmail(request.getEmail()))
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    if (userService.existByUsername(request.getUsername()))
      throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);

    User user = buildUser(request);
    return userService.save(user);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  private User buildUser(CreationRequest request) {
    User user = mapper.fromCreationRequest(request);
    user.setRoles(Set.of(Role.USER));
    user.setHashedPassword(encodePassword(request.getPassword()));
    return user;
  }
}
