package com.sam.sup.security.exception;

import com.sam.sup.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthResponder {
  void sendError(HttpServletResponse servletResponse, ErrorCode errorCode, String path)
      throws IOException;
}
