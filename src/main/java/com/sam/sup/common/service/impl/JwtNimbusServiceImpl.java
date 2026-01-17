package com.sam.sup.common.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sam.sup.common.config.AppProperties;
import com.sam.sup.common.service.JwtService;
import com.sam.sup.modules.user.entity.User;
import com.sam.sup.common.util.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtNimbusServiceImpl implements JwtService {
    final SecretKey secretKey;
    final AppProperties appProperties;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expirationTime = new Date(System.currentTimeMillis() + appProperties.getAccessTokenExpiration());

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("sup")
                .subject(user.getId().toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(Util.randomUUID())
                .claim("role", user.getRoles().stream().map(Enum::name).toList())
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
