package com.example.springawsdocker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExceptionResponse implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;

   private Date timestamp;
   private String message;
   private String details;
}
