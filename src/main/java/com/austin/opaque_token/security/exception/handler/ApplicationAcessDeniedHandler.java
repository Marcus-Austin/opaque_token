package com.austin.opaque_token.security.exception.handler;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.austin.opaque_token.dto.error.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationAcessDeniedHandler implements AccessDeniedHandler{

    private final ObjectMapper objectMapper;

    public ApplicationAcessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

     @Override
     public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

      log.error("Access denied for request: {}", request, accessDeniedException);

      ApiErrorResponse apiErrorResponse = new ApiErrorResponse(accessDeniedException.getMessage());

      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      objectMapper.writeValue(response.getOutputStream(), apiErrorResponse);
  }
}
