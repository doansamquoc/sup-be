package com.sam.sup.session.service;

import com.sam.sup.session.entity.Session;
import com.sam.sup.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface SessionService {
    Session create(User user, String ipAddress, String userAgent);

    void revoke(Session session);

    void revokeByToken(String token);

    void revokeAllByUser(User user);

    Session findByToken(String token);
}
