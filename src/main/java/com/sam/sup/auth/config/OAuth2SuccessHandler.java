package com.sam.sup.auth.config;

import com.sam.sup.core.constant.AppConstant;
import com.sam.sup.core.service.JwtService;
import com.sam.sup.session.entity.Session;
import com.sam.sup.session.service.SessionService;
import com.sam.sup.user.entity.User;
import com.sam.sup.user.service.UserService;
import com.sam.sup.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
  JwtService jwtService;
  SessionService sessionService;
  UserService userService;

  @Override
  public void onAuthenticationSuccess(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Authentication authentication)
      throws IOException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    User user = userService.processOAuthLogin(oAuth2User);

    String accessToken = jwtService.generateAccessToken(user);
    Session session =
        sessionService.create(user, Util.getIpAddress(request), Util.getUserAgent(request));
    String refreshToken = session.getToken();
    String targetUrl =
        UriComponentsBuilder.fromUriString("")
            .queryParam("accessToken", accessToken)
            .build()
            .toUriString();

    response.setHeader(AppConstant.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    clearAuthenticationAttributes(request);
    log.info("Target URL: {}", targetUrl);
    response.sendRedirect(targetUrl);
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
  }
}
