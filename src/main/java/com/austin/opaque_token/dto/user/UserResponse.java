package com.austin.opaque_token.dto.user;

import java.util.List;

import com.austin.opaque_token.common.Role;

public record UserResponse(
    String id,
    String username,
    String firstName,
    String lastName,
    List<Role> roles,
    Boolean active
) {

}
