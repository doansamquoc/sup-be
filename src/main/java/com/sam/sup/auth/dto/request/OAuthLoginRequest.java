package com.sam.sup.auth.dto.request;

import com.sam.sup.core.enums.LoginProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthLoginRequest {
    @NotBlank(message = "TOKEN_CANNOT_BLANK")
    String idToken;

    @NotNull(message = "LOGIN_PROVIDER_CANNOT_BLANK")
    LoginProvider provider;
}
