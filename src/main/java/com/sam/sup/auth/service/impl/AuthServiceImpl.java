package com.sam.sup.auth.service.impl;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.auth.dto.request.LoginRequest;
import com.sam.sup.auth.dto.response.AuthResponse;
import com.sam.sup.auth.entity.Session;
import com.sam.sup.auth.mapper.AuthMapper;
import com.sam.sup.auth.repository.SessionRepository;
import com.sam.sup.auth.service.AuthService;
import com.sam.sup.auth.service.SessionService;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.enums.Role;
import com.sam.sup.core.exception.BusinessException;
import com.sam.sup.core.service.JwtService;
import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.entity.User;
import com.sam.sup.user.service.UserService;
import com.sam.sup.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    SessionRepository sessionRepository;
    UserService userService;
    SessionService sessionService;
    JwtService jwtService;
    AuthMapper mapper;

    private User authenticate(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword())
            );
            return (User) auth.getPrincipal();
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    public AuthResponse login(
            LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        User user = authenticate(request);
        String accessToken = generateAccessToken(user);
        Session session = sessionService.create(user, servletRequest);

        ResponseCookie sessionCookie = CookieUtil.createSessionToken(session.getToken());
        CookieUtil.addCookie(servletResponse, sessionCookie);

        return AuthResponse.builder().accessToken(accessToken).build();
    }

    @Override
    public AuthResponse signup(
            CreationRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        if (userService.existByEmail(request.getEmail()))
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);

        if (userService.existByUsername(request.getUsername()))
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);

        User user = buildUser(request);
        UserDTO userSaved = userService.save(user);

        // Build for login
        LoginRequest loginRequest = LoginRequest.builder()
                .identifier(userSaved.getEmail())
                .password(request.getPassword())
                .build();

        return login(loginRequest, servletRequest, servletResponse);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private User buildUser(CreationRequest request) {
        User user = mapper.fromCreationRequest(request);
        user.setRoles(buildRoles());
        user.setHashedPassword(encodePassword(request.getPassword()));
        return user;
    }

    private Set<Role> buildRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        return roles;
    }

    private String generateAccessToken(User user) {
        return jwtService.generateAccessToken(user);
    }
}
