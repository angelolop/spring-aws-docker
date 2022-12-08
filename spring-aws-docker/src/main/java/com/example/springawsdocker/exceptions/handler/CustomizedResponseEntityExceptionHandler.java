package com.example.springawsdocker.exceptions.handler;

import com.example.springawsdocker.exceptions.ExceptionResponse;
import com.example.springawsdocker.exceptions.InvalidJwtAuthenticationException;
import com.example.springawsdocker.exceptions.RequiredObjectIsNullException;
import com.example.springawsdocker.exceptions.ResourceNotFoundException;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

   @ExceptionHandler(Exception.class)
   @ResponseStatus(INTERNAL_SERVER_ERROR)
   public final ExceptionResponse handleAllExceptions(Exception ex, WebRequest request) {
      return new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
   }

   @ExceptionHandler(ResourceNotFoundException.class)
   @ResponseStatus(NOT_FOUND)
   public final ExceptionResponse handleNotFoundExceptions(Exception ex, WebRequest request) {
      return new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
   }

   @ExceptionHandler(RequiredObjectIsNullException.class)
   @ResponseStatus(BAD_REQUEST)
   public final ExceptionResponse handleBadRequestException(Exception ex, WebRequest request) {
      return new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
   }

   @ExceptionHandler(InvalidJwtAuthenticationException.class)
   @ResponseStatus(FORBIDDEN)
   public ExceptionResponse handleInvalidJwtAuthenticationException(Exception ex, WebRequest request){
      return new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
   }

}
