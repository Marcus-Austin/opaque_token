package com.austin.opaque_token.security.user;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class AuthUserCache {

    private final Map <String, AuthUser> sessions = new ConcurrentHashMap<>();

    public void login(String token, AuthUser authUser) {
        sessions.put(token, authUser);
    }

    public void logout(String token) {
        sessions.remove(token);
    }

    public Optional<AuthUser> getByToken(String token){
     return Optional.ofNullable(sessions.get(token));
    }



}
