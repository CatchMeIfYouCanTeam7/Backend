package com.sparta.catchme.controller;

import com.sparta.catchme.dto.response.ResponseDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseDto<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
    String errorMessage = exception.getBindingResult()
        .getAllErrors()
        .get(0)
        .getDefaultMessage();

    return ResponseDto.fail("BAD_REQUEST", errorMessage);
  }

}
