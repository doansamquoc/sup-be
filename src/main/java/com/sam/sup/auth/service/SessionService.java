package com.sam.sup.auth.service;

import com.sam.sup.auth.entity.Session;
import com.sam.sup.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface SessionService {
    Session create(User user, HttpServletRequest servletRequest);

    void revoke(Session session);

    void revokeByToken(String token);

    void revokeAllByUser(User user);

    Session findByToken(String token);
}
