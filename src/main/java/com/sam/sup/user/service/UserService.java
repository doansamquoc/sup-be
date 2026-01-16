package com.sam.sup.user.service;

import com.sam.sup.auth.dto.SocialUserDto;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

    User processSocialUser(SocialUserDto socialUser);

    User processOAuthLogin(OAuth2User oAuth2User);

    UserResponse save(User user);

    User findByIdentifier(String identifier);

    User findByEmail(String email);

    boolean existByEmail(String email);

    boolean existByUsername(String username);
}
