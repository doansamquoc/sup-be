package com.sam.sup.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class Util {
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUserAgent(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("User-Agent");
    }

    public static String getIpAddress(HttpServletRequest servletRequest) {
        return servletRequest.getRemoteAddr();
    }
}
