package com.sam.sup.user.service;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.user.entity.User;

public interface UserService {

    UserResponse save(User user);

    User findByIdentifier(String identifier);

    boolean existByEmail(String email);

    boolean existByUsername(String username);
}
