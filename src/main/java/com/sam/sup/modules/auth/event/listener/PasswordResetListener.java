package com.sam.sup.modules.auth.event.listener;

import com.sam.sup.modules.auth.event.ForgotPasswordEvent;
import com.sam.sup.modules.mail.dto.request.SendMailRequest;
import com.sam.sup.modules.mail.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetListener {
  MailService mailService;

  @Async
  @EventListener
  public void handlePasswordResetListener(ForgotPasswordEvent event) {
    SendMailRequest request =
        SendMailRequest.builder()
            .to(event.getEmail())
            .token(event.getToken())
            .ip(event.getIp())
            .agent(event.getAgent())
            .build();
    mailService.sendPasswordReset(request);
  }
}
