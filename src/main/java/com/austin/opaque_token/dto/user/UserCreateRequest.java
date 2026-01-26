package com.austin.opaque_token.dto.user;

public record UserCreateRequest(
    String username, String password, String firstName, String lastName
) {

}
