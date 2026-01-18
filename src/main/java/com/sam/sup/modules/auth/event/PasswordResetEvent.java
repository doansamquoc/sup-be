package com.sam.sup.modules.auth.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetEvent extends ApplicationEvent {
  String email;
  String token;
  String ip;
  String agent;

  public PasswordResetEvent(Object source, String email, String token, String ip, String agent) {
    super(source);
    this.email = email;
    this.token = token;
    this.ip = ip;
    this.agent = agent;
  }
}
