package com.sam.sup.auth.service;

import com.sam.sup.auth.dto.SocialUserDto;
import com.sam.sup.core.enums.LoginProvider;

public interface SocialLoginStrategy {
    LoginProvider getProviderName();
    SocialUserDto verifyToken(String token);
}
