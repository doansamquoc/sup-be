package com.sam.sup.core.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
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
    return Keys.hmacShaKeyFor(appProperties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
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
