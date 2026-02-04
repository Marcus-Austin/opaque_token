package com.austin.opaque_token.service;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.austin.opaque_token.dto.user.UserResponse;
import com.austin.opaque_token.dto.user.UserResponseWithCredentials;
import com.austin.opaque_token.security.dto.LoginDto;
import com.austin.opaque_token.security.dto.TokenDto;
import com.austin.opaque_token.security.exception.ApplicationAuthenticationException;
import com.austin.opaque_token.security.user.AuthUser;
import com.austin.opaque_token.security.user.AuthUserCache;
@Service
public class AuthService {

    private final AuthUserCache authUserCache;

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  public AuthService(
      AuthUserCache authUserCache, UserService userService, PasswordEncoder passwordEncoder) {
        this.authUserCache = authUserCache;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDto login(LoginDto loginDto) {

        UserResponseWithCredentials userCredentials =
            userService.getUserCredentialsByUsername(loginDto.username());

        if (!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
        throw new ApplicationAuthenticationException("Password is incorrect");
        }

        String token = UUID.randomUUID().toString();
        UserResponse userResponse = userCredentials.userResponse();
        AuthUser authUser = new AuthUser(userResponse.id(), userResponse.roles());

        authUserCache.login(token, authUser);

        return new TokenDto(token);
    }

    public void logout(String token) {

        authUserCache.logout(token);
}

}
