package com.austin.opaque_token.dto.user;

public record UserResponseWithCredentials(UserResponse userResponse, String passwordHash) {

}
