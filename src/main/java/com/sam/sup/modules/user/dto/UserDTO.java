package com.sam.sup.modules.user.dto;

import com.sam.sup.common.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String username;
    String hashedPassword;
    String email;
    Set<Role> roles;
}
