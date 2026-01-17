package com.sam.sup.common.service;

import com.nimbusds.jose.JOSEException;
import com.sam.sup.modules.user.entity.User;

import java.text.ParseException;

public interface JwtService {

    String generateAccessToken(User user);

    boolean validateAccessToken(String token) throws ParseException, JOSEException;
}
