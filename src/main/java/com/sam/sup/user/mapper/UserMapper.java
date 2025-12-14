package com.sam.sup.user.mapper;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.user.dto.UserDTO;
import com.sam.sup.user.dto.response.UserResponse;
import com.sam.sup.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDTO toUserDTO(User user);
    UserResponse toUserResponse(User user);
    User fromCreationRequest(CreationRequest request);
}
