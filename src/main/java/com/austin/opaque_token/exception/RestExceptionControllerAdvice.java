package com.austin.opaque_token.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.austin.opaque_token.dto.error.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionControllerAdvice {
    
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiErrorResponse handleAuthenticationException(AuthenticationException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorResponse unhandledException(Exception ex) {
    return getApiErrorResponse(ex);
  }

  private ApiErrorResponse getApiErrorResponse(Exception ex) {

    log.error("Exception occurred", ex);

    return new ApiErrorResponse(ex.getMessage());
  }


}
