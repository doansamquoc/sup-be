package com.sam.sup.user.service.impl;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.exception.BusinessException;
import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.user.entity.User;
import com.sam.sup.user.mapper.UserMapper;
import com.sam.sup.user.repository.UserRepository;
import com.sam.sup.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
  UserRepository repository;
  UserMapper mapper;

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
  public boolean existByEmail(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public boolean existByUsername(String username) {
    return repository.existsByUsername(username);
  }
}
