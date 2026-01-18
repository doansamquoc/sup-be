package com.sam.sup.modules.mail.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailRequest {
  String to;
  String subject;
  String token;
  String text;
  String ip;
  String agent;
}
