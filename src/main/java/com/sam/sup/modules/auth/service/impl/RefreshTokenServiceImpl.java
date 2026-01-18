package com.sam.sup.modules.auth.service.impl;

import com.sam.sup.common.config.AppProperties;
import com.sam.sup.modules.auth.entity.RefreshToken;
import com.sam.sup.modules.auth.repository.RefreshTokenRepository;
import com.sam.sup.modules.auth.service.RefreshTokenService;
import com.sam.sup.common.enums.ErrorCode;
import com.sam.sup.common.exception.BusinessException;
import com.sam.sup.modules.user.entity.User;
import com.sam.sup.common.util.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
  RefreshTokenRepository repository;
  AppProperties appProperties;

  @Override
  public void validate(RefreshToken refreshToken) {
    if (refreshToken.isRevoked()) {
      throw new BusinessException(ErrorCode.TOKEN_REVOKED);
    } else if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
      throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
    }
  }

  @Override
  public RefreshToken create(User user, String ipAddress, String userAgent) {
    RefreshToken session =
        RefreshToken.builder()
            .user(user)
            .token(Util.randomUUID())
            .expiresAt(Instant.now().plusMillis(appProperties.getRefreshTokenExpiration()))
            .clientInfo(userAgent)
            .ipAddress(ipAddress)
            .revoked(false)
            .build();

    return repository.save(session);
  }

  @Override
  public void revoke(RefreshToken session) {
    session.setRevoked(true);
    repository.save(session);
  }

  @Override
  public void revokeByToken(String token) {
    repository.findByToken(token).ifPresent(this::revoke);
  }

  @Override
  public void revokeAllByUser(User user) {
    List<RefreshToken> validSessions =
        repository.findAllByUserAndRevokedFalseAndExpiresAtAfter(user, Instant.now());
    if (validSessions.isEmpty()) return;
    validSessions.forEach(session -> session.setRevoked(true));
    repository.saveAll(validSessions);
  }

  @Override
  public RefreshToken findByToken(String token) {
    return repository
        .findByToken(token)
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
  }
}
