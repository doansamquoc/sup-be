package com.sam.sup.modules.auth.service;

import com.sam.sup.modules.auth.entity.RefreshToken;
import com.sam.sup.modules.user.entity.User;

public interface RefreshTokenService {
    RefreshToken create(User user, String ipAddress, String userAgent);

    void revoke(RefreshToken session);

    void revokeByToken(String token);

    void revokeAllByUser(User user);

    RefreshToken findByToken(String token);
}
