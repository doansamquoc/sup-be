package com.sam.sup.auth.service.impl;

import com.sam.sup.auth.entity.Session;
import com.sam.sup.auth.repository.SessionRepository;
import com.sam.sup.auth.service.SessionService;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.exception.BusinessException;
import com.sam.sup.user.entity.User;
import com.sam.sup.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionServiceImpl implements SessionService {
    final SessionRepository repository;
    @Value("${app.refresh-token-expiration}")
    static long TOKEN_EXPIRATION;

    @Override
    public Session create(User user, HttpServletRequest servletRequest) {
        Session session = Session.builder()
                .user(user)
                .token(Util.randomUUID())
                .expiresAt(Instant.now().minusMillis(TOKEN_EXPIRATION))
                .clientInfo(Util.getUserAgent(servletRequest))
                .ipAddress(Util.getIpAddress(servletRequest))
                .revoked(false)
                .build();

        return repository.save(session);
    }

    @Override
    public void revoke(Session session) {
        session.setRevoked(true);
        repository.save(session);
    }

    @Override
    public void revokeByToken(String token) {
        repository.findByToken(token).ifPresent(this::revoke);
    }

    @Override
    public void revokeAllByUser(User user) {
        List<Session> validSessions = repository.findAllByUserAndRevokedFalseAndExpiresAtAfter(user, Instant.now());
        if (validSessions.isEmpty()) return;
        validSessions.forEach(session -> session.setRevoked(true));
        repository.saveAll(validSessions);
    }

    @Override
    public Session findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }
}
