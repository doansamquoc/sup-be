package com.sam.sup.utils;

import com.sam.sup.core.config.AppConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CookieUtil {
    @Value("${app.session-token-expiration}")
    static long TOKEN_EXPIRATION;

    // Extract cookie each request to server
    public static String extractSessionToken(HttpServletRequest servletRequest) {
        if (servletRequest.getCookies() != null)
            for (Cookie cookie : servletRequest.getCookies()) {
                if (AppConstant.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        return null;
    }

    private static ResponseCookie build(String name, String value, long maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .path(path)
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();
    }

    public static ResponseCookie create(String name, String value, long maxAge, String path) {
        return build(name, value, maxAge, path);
    }

    public static ResponseCookie createSessionToken(String value) {
        return create(AppConstant.REFRESH_TOKEN_COOKIE_NAME, value, TOKEN_EXPIRATION / 1000, "/");
    }

    public static ResponseCookie deleteSessionToken() {
        return delete(AppConstant.REFRESH_TOKEN_COOKIE_NAME, "/");
    }

    public static ResponseCookie delete(String name, String path) {
        return build(name, null, 0, path);
    }

    public static void addCookie(HttpServletResponse servletResponse, ResponseCookie cookie) {
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static Optional<Cookie> getCookie(HttpServletRequest servletRequest, String name) {
        if (servletRequest.getCookies() == null) return Optional.empty();
        return Arrays.stream(servletRequest.getCookies()).filter(cookie -> cookie.getName().equals(name)).findFirst();
    }
}
