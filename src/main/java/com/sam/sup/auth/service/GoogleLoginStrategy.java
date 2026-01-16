package com.sam.sup.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sam.sup.auth.dto.OAuthUserDto;
import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.enums.LoginProvider;
import com.sam.sup.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleLoginStrategy implements OAuthLoginStrategy {
  GoogleIdTokenVerifier googleIdTokenVerifier;

  @Override
  public LoginProvider getProviderName() {
    return LoginProvider.GOOGLE;
  }

  @Override
  public OAuthUserDto verifyToken(String token) {
    try {
      GoogleIdToken idToken = googleIdTokenVerifier.verify(token);
      if (idToken == null) throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);

      GoogleIdToken.Payload payload = idToken.getPayload();

      return OAuthUserDto.builder()
          .email(payload.getEmail())
          .fullName((String) payload.get("name"))
          .avatar((String) payload.get("picture"))
          .build();
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
    }
  }
}
