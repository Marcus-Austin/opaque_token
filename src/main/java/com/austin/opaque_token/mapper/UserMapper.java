package com.austin.opaque_token.mapper;

import org.mapstruct.Mapper;

import com.austin.opaque_token.dto.user.UserResponse;
import com.austin.opaque_token.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserEntity userEntity);

}
