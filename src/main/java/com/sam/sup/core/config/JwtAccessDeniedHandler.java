package com.sam.sup.core.config;

import com.sam.sup.core.dto.api.ErrorResult;
import com.sam.sup.core.dto.api.ResultFactory;
import com.sam.sup.core.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException)
      throws IOException {
    ErrorCode code = ErrorCode.ACCESS_DENIED;

    JwtAuthenticationEntryPoint.OAuthFilter(request, response, code);
  }
}
