package com.sam.sup.common.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BeanConfig {
  AppProperties appProperties;

  @Bean
  SecretKey secretKey() {
    byte[] key = appProperties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8);
    if (key.length < 32) throw new IllegalArgumentException("JWT secret key must be least 256 bit");
    return new SecretKeySpec(key, "HmacSHA256");
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  GoogleIdTokenVerifier googleIdTokenVerifier() {
    log.info("Audience config = {}", appProperties.getGoogleClientId());

    return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singleton(appProperties.getGoogleClientId()))
        .setIssuers(List.of("accounts.google.com", "https://accounts.google.com"))
        .build();
  }
}
