package com.sam.sup.modules.auth.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthUserDto {
  String email;
  String fullName;
  String avatar;
  String providerId;
}
