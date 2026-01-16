package com.sam.sup.auth.service;

import com.sam.sup.core.enums.ErrorCode;
import com.sam.sup.core.enums.LoginProvider;
import com.sam.sup.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuthLoginStrategyFactory {
  Map<LoginProvider, OAuthLoginStrategy> strategies;

  public OAuthLoginStrategyFactory(List<OAuthLoginStrategy> strategyList) {
    Map<LoginProvider, OAuthLoginStrategy> map = new EnumMap<>(LoginProvider.class);
    for (OAuthLoginStrategy strategy : strategyList) {
      map.put(strategy.getProviderName(), strategy);
    }
    this.strategies = map;
  }

  public OAuthLoginStrategy getStrategy(LoginProvider provider) {
    OAuthLoginStrategy strategy = strategies.get(provider);
    if (strategy == null) throw new BusinessException(ErrorCode.PROVIDER_NOT_SUPPORTED);
    return strategy;
  }
}
