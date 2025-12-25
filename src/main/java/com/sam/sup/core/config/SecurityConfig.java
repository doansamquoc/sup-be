package com.sam.sup.core.config;

import com.sam.sup.auth.service.CustomUserDetailsService;
import com.sam.sup.core.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
  SecretKey secretKey;
  PasswordEncoder passwordEncoder;
  CustomUserDetailsService customUserDetailsService;
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
    httpSecurity.cors(cors -> cors.configurationSource(configurationSource()));
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.authorizeHttpRequests(
        auth -> {
          auth.requestMatchers(AppConstant.PUBLIC_ENDPOINTS).permitAll();
          auth.requestMatchers(AppConstant.SWAGGER_ENDPOINTS).permitAll();
          auth.anyRequest().authenticated();
        });

    httpSecurity.oauth2ResourceServer(
        oauth2 -> {
          oauth2.authenticationEntryPoint(jwtAuthenticationEntryPoint);
          oauth2.accessDeniedHandler(jwtAccessDeniedHandler);
          oauth2.jwt(
              jwt -> {
                jwt.decoder(jwtDecoder());
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
              });
        });
    // Session stateless because using JWT for authentication and authorization
    httpSecurity.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return httpSecurity.build();
  }

  @Bean
  CorsConfigurationSource configurationSource() {
    CorsConfiguration cors = new CorsConfiguration();
    cors.addAllowedOrigin("http://localhost:5173");
    cors.addAllowedHeader("*");
    cors.addAllowedMethod("*");
    cors.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cors);
    return source;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
  }

  @Bean
  AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider(customUserDetailsService);
    dao.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(dao);
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthoritiesClaimName("role");
    authoritiesConverter.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return authenticationConverter;
  }
}
