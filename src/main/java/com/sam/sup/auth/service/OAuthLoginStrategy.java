package com.sam.sup.auth.service;

import com.sam.sup.auth.dto.OAuthUserDto;
import com.sam.sup.core.enums.OAuthProvider;

public interface OAuthLoginStrategy {
    OAuthProvider getProviderName();
    OAuthUserDto verifyToken(String token);
}
