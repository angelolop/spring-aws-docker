package com.example.springawsdocker.exceptions;


import java.io.Serial;

public class MyFileNotFoundException extends RuntimeException {

   @Serial
   private static final long serialVersionUID = 1L;

   public MyFileNotFoundException(String msg) {
      super(msg);
   }

   public MyFileNotFoundException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
