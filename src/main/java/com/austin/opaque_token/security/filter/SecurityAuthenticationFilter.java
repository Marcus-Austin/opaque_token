package com.austin.opaque_token.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.austin.opaque_token.common.AuthConstants;
import com.austin.opaque_token.security.authentication.UserAuthentication;
import com.austin.opaque_token.security.exception.TokenAuthenticationException;
import com.austin.opaque_token.security.user.AuthUser;
import com.austin.opaque_token.security.user.AuthUserCache;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private final AuthUserCache authUserCache;

    public SecurityAuthenticationFilter(AuthUserCache authUserCache) {
        this.authUserCache = authUserCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
              String authenticationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);  

              if(authenticationHeader == null){
                filterChain.doFilter(request, response);
                return;
              }

              AuthUser authUser = 
               authUserCache
                .getByToken(authenticationHeader)
                 .orElseThrow(() -> 
                  new TokenAuthenticationException("Invalid authentication token"));

                  UserAuthentication authentication  = new UserAuthentication(authUser);

                  SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                  securityContext.setAuthentication(authentication);
                  SecurityContextHolder.setContext(securityContext);

                  filterChain.doFilter(request, response);
    }
}
