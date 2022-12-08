package com.example.springawsdocker.exceptions;


public class RequiredObjectIsNullException extends RuntimeException {

   public RequiredObjectIsNullException() {
      super("It is not allowed to persist a null object");
   }

   public RequiredObjectIsNullException(String msg) {
      super(msg);
   }

}
