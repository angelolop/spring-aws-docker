package com.example.springawsdocker.exceptions;


import java.io.Serial;

public class FileStorageException extends RuntimeException {

   @Serial
   private static final long serialVersionUID = 1L;

   public FileStorageException(String msg) {
      super(msg);
   }

   public FileStorageException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
