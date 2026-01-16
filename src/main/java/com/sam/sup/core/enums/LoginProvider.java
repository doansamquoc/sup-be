package com.sam.sup.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sam.sup.core.exception.BusinessException;

public enum LoginProvider {
  GOOGLE,
  FACEBOOK,
  GITHUB;

  @JsonCreator
  public static LoginProvider from(String value) {
    for (LoginProvider p : values()) {
      if (p.name().equalsIgnoreCase(value)) {
        return p;
      }
    }
    throw new BusinessException(ErrorCode.PROVIDER_NOT_SUPPORTED);
  }
}
