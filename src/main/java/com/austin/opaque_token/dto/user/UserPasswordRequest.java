package com.austin.opaque_token.dto.user;

public record UserPasswordRequest(String oldPassword, String newPassword) {

}
