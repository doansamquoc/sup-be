package com.sam.sup.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sam.sup.common.exception.BusinessException;

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
