package com.sam.sup.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreationRequest {
    @Size(min = 3, max = 24, message = "USERNAME_CONDITIONS")
    @NotBlank(message = "USERNAME_CANNOT_BLANK")
    String username;

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_CANNOT_BLANK")
    String email;

    @NotBlank(message = "DISPLAY_NAME_CANNOT_BLANK")
    String displayName;

    @Size(min = 6, max = 24, message = "PASSWORD_CONDITIONS")
    @NotBlank(message = "PASSWORD_CANNOT_BLANK")
    String password;
}
