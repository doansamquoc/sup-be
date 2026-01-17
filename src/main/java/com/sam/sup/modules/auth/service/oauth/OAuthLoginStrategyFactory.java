package com.sam.sup.modules.auth.service.oauth;

import com.sam.sup.common.enums.ErrorCode;
import com.sam.sup.common.enums.OAuthProvider;
import com.sam.sup.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuthLoginStrategyFactory {
  Map<OAuthProvider, OAuthLoginStrategy> strategies;

  public OAuthLoginStrategyFactory(List<OAuthLoginStrategy> strategyList) {
    Map<OAuthProvider, OAuthLoginStrategy> map = new EnumMap<>(OAuthProvider.class);
    for (OAuthLoginStrategy strategy : strategyList) {
      map.put(strategy.getProviderName(), strategy);
    }
    this.strategies = map;
  }

  public OAuthLoginStrategy getStrategy(OAuthProvider provider) {
    OAuthLoginStrategy strategy = strategies.get(provider);
    if (strategy == null) throw new BusinessException(ErrorCode.PROVIDER_NOT_SUPPORTED);
    return strategy;
  }
}
