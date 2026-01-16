package com.sam.sup.core.audit;

import com.sam.sup.core.annotation.ClientIp;
import com.sam.sup.core.annotation.UserAgent;
import com.sam.sup.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuditMetaDataResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.hasParameterAnnotation(ClientIp.class)
        || parameter.hasParameterAnnotation(UserAgent.class);
  }

  @Override
  public @Nullable Object resolveArgument(
      @NonNull MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) {

    HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

    if (servletRequest == null) return null;

    if (parameter.hasParameterAnnotation(ClientIp.class)) {
      return Util.getIpAddress(servletRequest);
    }

    if (parameter.hasParameterAnnotation(UserAgent.class)) {
      return Util.getUserAgent(servletRequest);
    }

    return null;
  }
}
