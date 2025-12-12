package com.sam.sup.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
    @SuppressWarnings("NullableProblems")
    UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException;
}
