package com.sam.sup.auth.repository;

import com.sam.sup.auth.entity.Session;
import com.sam.sup.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("NullableProblems")
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByToken(String token);
    Optional<Session> findByUser(User user);
    List<Session> findAllByUserAndRevokedFalseAndExpiresAtAfter(User user, Instant now);
}
