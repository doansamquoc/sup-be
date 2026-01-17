package com.sam.sup.modules.auth.mapper;

import com.sam.sup.modules.auth.dto.request.CreationRequest;
import com.sam.sup.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    User fromCreationRequest(CreationRequest request);
}
