package com.sam.sup.modules.auth.entity;

import com.sam.sup.common.entity.BaseEntity;
import com.sam.sup.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_tokens")
@Entity(name = "password_reset_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetToken extends BaseEntity {
  @Column(name = "token", nullable = false)
  String token;

  @Column(name = "expiry_date", nullable = false)
  Instant expiryDate;

  @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  public boolean isExpired() {
    return Instant.now().isAfter(this.expiryDate);
  }
}
