package com.austin.opaque_token.security.user;

import java.util.List;

import com.austin.opaque_token.common.Role;

public record AuthUser (String userId,List<Role> roles){

}
