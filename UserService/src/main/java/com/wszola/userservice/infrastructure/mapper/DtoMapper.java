package com.wszola.userservice.infrastructure.mapper;

import com.wszola.userservice.api.dto.UserResponse;
import com.wszola.userservice.infrastructure.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
		componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DtoMapper {
	UserResponse toUserResponse(User user);
}
