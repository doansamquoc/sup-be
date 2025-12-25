package com.sam.sup.core.config;

import com.sam.sup.core.dto.api.ErrorResult;
import com.sam.sup.core.dto.api.ResultFactory;
import com.sam.sup.core.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException)
      throws IOException {

    ErrorCode code = ErrorCode.UNAUTHORIZED;

    OAuthFilter(request, response, code);
  }

  static void OAuthFilter(
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, ErrorCode code)
      throws IOException {
    ErrorResult error = ResultFactory.error(code, request.getServletPath()).getBody();

    response.setStatus(code.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    ObjectMapper mapper = new ObjectMapper();
    response.getWriter().write(mapper.writeValueAsString(error));
  }
}
