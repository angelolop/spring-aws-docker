package com.example.springawsdocker.integrationtests.controller.withjson;

import com.example.springawsdocker.configs.TestConfigs;
import com.example.springawsdocker.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.springawsdocker.integrationtests.vo.AccountCredentialsVO;
import com.example.springawsdocker.integrationtests.vo.TokenVO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

   private static TokenVO tokenVO;

   @Test
   @Order(1)
   public void testSignin() throws IOException {
      AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

      tokenVO = given()
              .basePath("/auth/signin")
              .port(TestConfigs.SERVER_PORT)
              .contentType(TestConfigs.CONTENT_TYPE_JSON)
              .body(user)
              .when().post().then()
              .statusCode(200).extract().body().as(TokenVO.class);

      assertNotNull(tokenVO.getAccessToken());
      assertNotNull(tokenVO.getRefreshToken());
   }
   @Test
   @Order(2)
   public void testRefresh() throws IOException {
      AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

      var newTokenVO = given()
              .basePath("/auth/refresh")
              .port(TestConfigs.SERVER_PORT)
              .contentType(TestConfigs.CONTENT_TYPE_JSON)
              .pathParam("username", tokenVO.getUsername())
              .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
              .when().put("{username}").then()
              .statusCode(200).extract().body().as(TokenVO.class);

      assertNotNull(newTokenVO.getAccessToken());
      assertNotNull(newTokenVO.getRefreshToken());
   }
}
