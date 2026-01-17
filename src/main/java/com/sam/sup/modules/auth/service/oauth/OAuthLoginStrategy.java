package com.sam.sup.modules.auth.service.oauth;

import com.sam.sup.modules.auth.dto.internal.OAuthUserDto;
import com.sam.sup.common.enums.OAuthProvider;

public interface OAuthLoginStrategy {
    OAuthProvider getProviderName();
    OAuthUserDto verifyToken(String token);
}
