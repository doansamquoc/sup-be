package com.sam.sup.auth.service;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.auth.dto.request.LoginRequest;
import com.sam.sup.auth.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    AuthResponse signup(
            CreationRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    );
}
