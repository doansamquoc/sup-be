package com.sam.sup.security.exception;

import com.sam.sup.common.api.ErrorResult;
import com.sam.sup.common.api.ResultFactory;
import com.sam.sup.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAuthResponder implements AuthResponder {
  ObjectMapper mapper;

  @Override
  public void sendError(HttpServletResponse servletResponse, ErrorCode errorCode, String path)
      throws IOException {
    ErrorResult error = ResultFactory.error(errorCode, path).getBody();

    servletResponse.setStatus(errorCode.getHttpStatus().value());
    servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

    servletResponse.getWriter().write(mapper.writeValueAsString(error));
  }
}
