package com.sam.sup.auth.mapper;

import com.sam.sup.auth.dto.request.CreationRequest;
import com.sam.sup.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    User fromCreationRequest(CreationRequest request);
}
