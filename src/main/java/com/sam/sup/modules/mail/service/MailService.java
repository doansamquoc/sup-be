package com.sam.sup.modules.mail.service;

import com.sam.sup.modules.mail.dto.request.SendMailRequest;
import org.springframework.stereotype.Component;

@Component
public interface MailService {
    void sendPasswordChanged(SendMailRequest request);

    void sendPasswordReset(SendMailRequest request);
}
