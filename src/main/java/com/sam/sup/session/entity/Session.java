package com.sam.sup.session.entity;

import com.sam.sup.core.entity.BaseEntity;
import com.sam.sup.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sessions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "hashed_token", unique = true, nullable = false)
    String token;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    boolean revoked;

    @Column(name = "client_info")
    String clientInfo;

    @Column(name = "ip_address")
    String ipAddress;
}
