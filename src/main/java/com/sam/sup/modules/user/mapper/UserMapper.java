package com.sam.sup.modules.user.mapper;

import com.sam.sup.modules.auth.dto.request.CreationRequest;
import com.sam.sup.modules.user.dto.UserDTO;
import com.sam.sup.modules.user.dto.request.UserUpdateRequest;
import com.sam.sup.modules.user.dto.response.UserResponse;
import com.sam.sup.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  UserDTO toUserDTO(User user);

  UserResponse toUserResponse(User user);

  User fromCreationRequest(CreationRequest request);

  User toUser(OAuth2User oAuth2User);

  User fromUserUpdateRequest(UserUpdateRequest request, @MappingTarget User user);
}
