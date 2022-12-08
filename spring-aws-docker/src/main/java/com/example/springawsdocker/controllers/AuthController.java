package com.example.springawsdocker.controllers;

import com.example.springawsdocker.services.AuthServices;
import com.example.springawsdocker.vo.v1.security.AccountCredentialsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

   @Autowired
   AuthServices authServices;

   @SuppressWarnings("rawtypes")
   @Operation(summary = "Authenticates a user and returns a token")
   @PostMapping("/signin")
   public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
      if (checkIfParamsIsNotNull(data))
         return ResponseEntity.status(FORBIDDEN).body("Invalid client request!");

      var token = authServices.signin(data);
      if (token == null) {
         return ResponseEntity.status(FORBIDDEN).body("Invalid client request!");
      }

      return token;
   }

   @SuppressWarnings("rawtypes")
   @Operation(summary = "Refresh token for authenticated user and returns a token")
   @PutMapping(value = "/refresh/{username}")
   public ResponseEntity refreshToken(@PathVariable("username") String username,
                                      @RequestHeader("Authorization") String refreshToken) {
      if (checkIfParamsIsNotNull(username, refreshToken))
         return ResponseEntity.status(FORBIDDEN).body("Invalid client request!");
      var token = authServices.refreshToken(username, refreshToken);
      if (token == null) return ResponseEntity.status(FORBIDDEN).body("Invalid client request!");
      return token;
   }

   private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
      return refreshToken == null || refreshToken.isBlank() ||
              username == null || username.isBlank();
   }

   private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
      return data == null || data.getUsername() == null || data.getUsername().isBlank()
              || data.getPassword() == null || data.getPassword().isBlank();
   }
}
