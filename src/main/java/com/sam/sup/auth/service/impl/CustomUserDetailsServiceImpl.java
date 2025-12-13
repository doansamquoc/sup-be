package com.sam.sup.auth.service.impl;

import com.sam.sup.auth.service.CustomUserDetailsService;
import com.sam.sup.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    UserService userService;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String identifier) throws UsernameNotFoundException {
        return userService.findByIdentifier(identifier);
    }
}
