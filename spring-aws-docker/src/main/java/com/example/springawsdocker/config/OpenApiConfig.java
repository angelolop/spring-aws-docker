package com.example.springawsdocker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

   @Bean
   public OpenAPI customOpenAPI() {
      return new OpenAPI()
              .info(new Info()
                      .title("RestFul Api with Java 18 and Spring boot 3")
                      .version("v1")
                      .description("Project with Java")
                      .termsOfService("")
                      .license(new License()
                              .name("Apache 2.0")));
   }
}
