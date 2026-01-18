package com.sam.sup.modules.auth.service;

import com.sam.sup.modules.auth.dto.request.CreationRequest;
import com.sam.sup.modules.auth.dto.request.LoginRequest;
import com.sam.sup.modules.auth.dto.request.OAuthLoginRequest;
import com.sam.sup.modules.auth.dto.response.LoginResult;
import com.sam.sup.modules.user.dto.response.UserResponse;
import jakarta.transaction.Transactional;

public interface AuthService {
    LoginResult oAuthLogin(OAuthLoginRequest request, String ip, String agent);

    @Transactional
    LoginResult login(LoginRequest request, String ipAddress, String userAgent);

    UserResponse signup(CreationRequest request);

    void logout(String refreshToken);

    // Refresh token rotation
    LoginResult refresh(String refreshTokenString, String ip, String agent);
}
