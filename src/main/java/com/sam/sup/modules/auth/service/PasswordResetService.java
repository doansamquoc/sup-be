package com.sam.sup.modules.auth.service;

import com.sam.sup.modules.auth.dto.request.RequestResetPasswordRequest;
import com.sam.sup.modules.auth.dto.request.ResetPasswordRequest;
import org.springframework.stereotype.Component;

@Component
public interface PasswordResetService {
    String createTokenForUser(RequestResetPasswordRequest request);

    boolean validatePasswordResetToken(String token);

    void resetPassword(ResetPasswordRequest request);
}
