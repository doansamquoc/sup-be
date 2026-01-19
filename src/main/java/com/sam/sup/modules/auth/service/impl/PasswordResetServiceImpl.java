package com.sam.sup.modules.auth.service.impl;

import com.sam.sup.common.config.AppProperties;
import com.sam.sup.common.enums.ErrorCode;
import com.sam.sup.common.exception.BusinessException;
import com.sam.sup.modules.auth.dto.request.ForgotPasswordRequest;
import com.sam.sup.modules.auth.dto.request.ResetPasswordRequest;
import com.sam.sup.modules.auth.entity.PasswordResetToken;
import com.sam.sup.modules.auth.repository.PasswordResetTokenRepository;
import com.sam.sup.modules.auth.service.PasswordResetService;
import com.sam.sup.modules.user.entity.User;
import com.sam.sup.modules.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetServiceImpl implements PasswordResetService {
  PasswordResetTokenRepository repository;
  UserService userService;
  AppProperties appProperties;
  PasswordEncoder passwordEncoder;

  @Override
  public String createTokenForUser(ForgotPasswordRequest request) {
    User user = userService.findByEmail(request.getEmail());

    // Check old token
    // If token is existed, let check expiry date
    // If token has expired, delete it and create new token
    PasswordResetToken oldToken = findByUser(user);
    if (oldToken != null)
      if (!oldToken.isExpired()) return oldToken.getToken();
      else repository.delete(oldToken);

    String token = UUID.randomUUID().toString();
    PasswordResetToken passwordResetToken =
        PasswordResetToken.builder()
            .token(token)
            .user(user)
            .expiryDate(Instant.now().plusMillis(appProperties.getAccessTokenExpiration()))
            .build();
    repository.save(passwordResetToken);
    return token;
  }

  @Override
  public boolean validatePasswordResetToken(String token) {
    Optional<PasswordResetToken> passwordResetToken = repository.findByToken(token);
    return passwordResetToken.isPresent() && !passwordResetToken.get().isExpired();
  }

  @Override
  public void resetPassword(ResetPasswordRequest request) {
    PasswordResetToken passwordResetToken = findByToken(request.getToken());
    if (passwordResetToken.isExpired()) throw new BusinessException(ErrorCode.TOKEN_EXPIRED);

    User user = passwordResetToken.getUser();
    user.setHashedPassword(passwordEncoder.encode(request.getNewPassword()));
    userService.save(user);

    repository.delete(passwordResetToken);
  }

  private PasswordResetToken findByToken(String token) {
    return repository
        .findByToken(token)
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
  }

  private PasswordResetToken findByUser(User user) {
    return repository.findByUser(user).orElse(null);
  }
}
