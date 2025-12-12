package com.sam.sup.user.service;

import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.entity.User;

public interface UserService {

    UserDTO save(User user);

    User findByIdentifier(String identifier);

    boolean existByEmail(String email);

    boolean existByUsername(String username);
}
