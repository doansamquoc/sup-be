package com.sam.sup.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialUserDto {
  String email;
  String fullName;
  String avatar;
  String providerId;
}
