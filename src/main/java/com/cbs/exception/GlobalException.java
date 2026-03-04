package com.cbs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DublicateCustomerException.class)
  public ResponseEntity<ErrorResponse> handleDublicateCustomer(DublicateCustomerException exception)
  {
     ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
     return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse>  handleValidation(IllegalArgumentException exception)
  {
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.value());
      return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
  }


}
