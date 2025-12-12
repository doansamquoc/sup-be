package com.sam.sup.core.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sam.sup.core.service.JwtService;
import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.entity.User;
import com.sam.sup.utils.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtNimbusServiceImpl implements JwtService {
    final SecretKey secretKey;

    @Value("${app.access-token-expiration}")
    static long TOKEN_EXPIRATION;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expirationTime = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("sup")
                .subject(user.getId().toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(Util.randomUUID())
                .claim("scopes", user.getRoles().stream().map(Enum::name).toList())
                .claim("name", user.getDisplayName())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .build();

        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException exception) {
            log.error("Generate access token failed, error: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

        return jwsObject.serialize();
    }

    @Override
    public boolean validateAccessToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) return false;
        return signedJWT.verify(new MACVerifier(secretKey));
    }
}
