package com.sam.sup.session.service.impl;

import com.sam.sup.core.config.AppProperties;
import com.sam.sup.session.entity.Session;
import com.sam.sup.session.repository.SessionRepository;
import com.sam.sup.session.service.SessionService;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.exception.BusinessException;
import com.sam.sup.user.entity.User;
import com.sam.sup.utils.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionServiceImpl implements SessionService {
    SessionRepository repository;
    AppProperties appProperties;

    @Override
    public Session create(User user, String ipAddress, String userAgent) {
        Session session = Session.builder()
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
