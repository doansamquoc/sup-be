package com.sam.sup.auth.service;

import com.sam.sup.auth.dto.OAuthUserDto;
import com.sam.sup.core.enums.LoginProvider;

public interface OAuthLoginStrategy {
    LoginProvider getProviderName();
    OAuthUserDto verifyToken(String token);
}
