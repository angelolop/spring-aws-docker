package com.example.springawsdocker.vo.v2;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class PersonVOV2 implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;

   private Long id;

   private String firstName;

   private String lastName;

   private String address;

   private String gender;

   private Date birthday;
}
