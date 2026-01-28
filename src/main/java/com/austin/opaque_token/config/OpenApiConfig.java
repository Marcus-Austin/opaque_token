package com.austin.opaque_token.config;

import org.springframework.context.annotation.Configuration;

import com.austin.opaque_token.common.AuthConstants;
import com.austin.opaque_token.common.OpenApiConstants;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(
    name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = AuthConstants.AUTHORIZATION_HEADER
)
public class OpenApiConfig {

}
