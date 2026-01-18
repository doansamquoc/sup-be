package com.sam.sup.modules.user.service;

import com.sam.sup.modules.auth.dto.internal.OAuthUserDto;
import com.sam.sup.modules.user.dto.request.UserUpdateRequest;
import com.sam.sup.modules.user.dto.response.UserResponse;
import com.sam.sup.modules.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

    User processSocialUser(OAuthUserDto socialUser);

    User processOAuthLogin(OAuth2User oAuth2User);

    UserResponse updateById(Long id, UserUpdateRequest request);

    UserResponse save(User user);

    User findByIdentifier(String identifier);

    User findByEmail(String email);

    boolean existByEmail(String email);

    boolean existByUsername(String username);
}
