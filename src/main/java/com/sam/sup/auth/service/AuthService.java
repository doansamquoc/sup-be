package com.sam.sup.auth.service;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.auth.dto.request.LoginRequest;
import com.sam.sup.auth.dto.request.SocialLoginRequest;
import com.sam.sup.auth.dto.response.LoginResult;
import com.sam.sup.user.dto.response.UserResponse;
import jakarta.transaction.Transactional;

public interface AuthService {
    LoginResult loginSocial(SocialLoginRequest request, String ip, String agent);

    @Transactional
    LoginResult login(LoginRequest request, String ipAddress, String userAgent);

    UserResponse signup(CreationRequest request);

    void logout(String refreshToken);
}
