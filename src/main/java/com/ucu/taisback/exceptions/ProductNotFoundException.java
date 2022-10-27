package com.ucu.taisback.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception {

  public ProductNotFoundException(String errorMessage){
    super(errorMessage);
  }

}
