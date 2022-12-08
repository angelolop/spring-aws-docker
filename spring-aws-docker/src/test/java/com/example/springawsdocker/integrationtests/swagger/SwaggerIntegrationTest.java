package com.example.springawsdocker.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.springawsdocker.configs.TestConfigs;
import com.example.springawsdocker.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

   @Test
   public void shouldDisplaySwaggerUiPage() {
      var content = given()
                          .port(TestConfigs.SERVER_PORT)
                          .basePath("swagger-ui/index.html")
                          .when().get().then()
                          .statusCode(200).extract().body().asString();
      assertTrue(content.contains("Swagger UI"));
   }
}
