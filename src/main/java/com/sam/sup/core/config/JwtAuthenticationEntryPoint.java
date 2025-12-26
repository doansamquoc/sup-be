package com.sam.sup.core.config;

import com.sam.sup.core.dto.api.ErrorResult;
import com.sam.sup.core.dto.api.ResultFactory;
import com.sam.sup.core.dto.response.ResponseWriter;
import com.sam.sup.core.enums.ErrorCode;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  ResponseWriter writer;
  private final ServletResponse servletResponse;

  @Override
  public void commence(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException)
      throws IOException {
    ErrorCode code = ErrorCode.UNAUTHORIZED;
    writer.writeError(response, code, request.getServletPath());
  }
}
