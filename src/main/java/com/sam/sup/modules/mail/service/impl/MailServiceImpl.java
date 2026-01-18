package com.sam.sup.modules.mail.service.impl;

import com.sam.sup.common.constant.AppConstant;
import com.sam.sup.modules.mail.dto.request.SendMailRequest;
import com.sam.sup.modules.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {
  JavaMailSender mailSender;
  SpringTemplateEngine templateEngine;

  @Override
  public void sendPasswordChanged(SendMailRequest request) {
    String subject = "Password Changed";
    Context context = new Context();
    context.setVariable("token", request.getToken());

    request.setSubject(subject);

    sendHtmlMail(request, AppConstant.PASSWORD_CHANGED_TEMPLATE, context);
  }

  @Override
  public void sendPasswordReset(SendMailRequest request) {
    String subject = "Password Reset";
    Context context = new Context();
    context.setVariable("token", request.getToken());

    request.setSubject(subject);

    sendHtmlMail(request, AppConstant.PASSWORD_RESET_TEMPLATE, context);
  }

  private void sendHtmlMail(SendMailRequest request, String templateName, Context context) {
    try {
      context.setVariable("email", request.getTo());
      context.setVariable("ip", request.getIp());
      context.setVariable("agent", request.getAgent());
      context.setVariable("time", new Date());

      String htmlContent = templateEngine.process(templateName, context);
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(request.getTo());
      helper.setSubject(request.getSubject());
      helper.setText(htmlContent, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      log.error("Send Mail Failed: {}", e.getMessage());
    }
  }
}
