package com.example.library.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ErrorHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<String>> handle(RuntimeException ex) {
    LOGGER.error(ex.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<String>> handle(Exception ex) {
    LOGGER.error(ex.getMessage());
    return Mono.just(ResponseEntity.badRequest().build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public Mono<ResponseEntity<String>> handle(AccessDeniedException ex) {
    LOGGER.error(ex.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
  }
}
