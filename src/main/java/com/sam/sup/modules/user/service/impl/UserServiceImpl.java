package com.sam.sup.modules.user.service.impl;

import com.sam.sup.modules.auth.dto.internal.OAuthUserDto;
import com.sam.sup.common.enums.ErrorCode;
import com.sam.sup.common.enums.Role;
import com.sam.sup.common.exception.BusinessException;
import com.sam.sup.modules.user.dto.response.UserResponse;
import com.sam.sup.modules.user.entity.User;
import com.sam.sup.modules.user.mapper.UserMapper;
import com.sam.sup.modules.user.repository.UserRepository;
import com.sam.sup.modules.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
  UserRepository repository;
  UserMapper mapper;

  @Override
  public User processSocialUser(OAuthUserDto socialUser) {
    return repository
        .findByEmail(socialUser.getEmail())
        .orElseGet(
            () -> {
              User newUser =
                  User.builder()
                      .displayName(socialUser.getFullName())
                      .email(socialUser.getEmail())
                      .roles(Set.of(Role.USER))
                      .verified(true)
                      .build();
              return repository.save(newUser);
            });
  }

  @Override
  public User processOAuthLogin(OAuth2User oAuth2User) {
    String email = oAuth2User.getAttribute("email");
    if (email == null) {
      throw new OAuth2AuthenticationException("Email Not found from OAuth2 provider");
    }
    return repository
        .findByEmail(email)
        .map(
            existing -> {
              // Update provider or do something
              return existing;
            })
        .orElseGet(
            () -> {
              User user = mapper.toUser(oAuth2User);
              return repository.save(user);
            });
  }

  @Override
  public UserResponse save(User user) {
    return mapper.toUserResponse(repository.save(user));
  }

  @Override
  public User findByIdentifier(String identifier) {
    return repository
        .findByIdentifier(identifier)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public User findByEmail(String email) {
    return repository
        .findByEmail(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public boolean existByEmail(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public boolean existByUsername(String username) {
    return repository.existsByUsername(username);
  }
}
