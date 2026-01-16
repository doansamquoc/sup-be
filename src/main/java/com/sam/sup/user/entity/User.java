package com.sam.sup.user.entity;

import com.sam.sup.core.entity.BaseEntity;
import com.sam.sup.core.enums.OAuthProvider;
import com.sam.sup.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {
  @Column(name = "username", unique = true, nullable = false)
  String username;

  @Column(name = "hashed_password", nullable = false)
  String hashedPassword;

  @Column(name = "display_name")
  String displayName;

  @Column(name = "email", unique = true)
  String email;

  @Column(name = "roles")
  @Enumerated(EnumType.STRING)
  Set<Role> roles;

  @Column(name = "verified")
  boolean verified;

  @Column(name = "providers")
  @Enumerated(EnumType.STRING)
  Set<OAuthProvider> providers;

  @Override
  @NullMarked
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles.isEmpty()) return Collections.emptySet();
    return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList();
  }

  @Override
  public @Nullable String getPassword() {
    return hashedPassword;
  }
}
