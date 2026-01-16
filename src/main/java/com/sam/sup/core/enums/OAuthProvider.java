package com.sam.sup.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sam.sup.core.exception.BusinessException;

public enum OAuthProvider {
  GOOGLE,
  FACEBOOK,
  GITHUB;

  @JsonCreator
  public static OAuthProvider from(String value) {
    for (OAuthProvider p : values()) {
      if (p.name().equalsIgnoreCase(value)) {
        return p;
      }
    }
    throw new BusinessException(ErrorCode.PROVIDER_NOT_SUPPORTED);
  }
}
