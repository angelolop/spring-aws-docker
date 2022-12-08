package com.example.springawsdocker.integrationtests.controller.withyaml;

import com.example.springawsdocker.configs.TestConfigs;
import com.example.springawsdocker.integrationtests.controller.withyaml.mapper.YamlMapper;
import com.example.springawsdocker.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.springawsdocker.integrationtests.vo.AccountCredentialsVO;
import com.example.springawsdocker.integrationtests.vo.PersonVO;
import com.example.springawsdocker.integrationtests.vo.TokenVO;
import com.example.springawsdocker.integrationtests.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

   private static RequestSpecification specification;
   private static YamlMapper objectMapper;

   private static PersonVO person;

   @BeforeAll
   public static void setup() {
      objectMapper = new YamlMapper();

      person = new PersonVO();
   }

   @Test
   @Order(0)
   public void authorization() throws IOException {
      AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

      var accessToken = given()
                               .config(RestAssuredConfig.config()
                                       .encoderConfig(EncoderConfig.encoderConfig()
                                               .encodeContentTypeAs(
                                                       TestConfigs.CONTENT_TYPE_YML,
                                                       ContentType.TEXT
                                               )))
                               .basePath("/auth/signin")
                               .port(TestConfigs.SERVER_PORT)
                               .contentType(TestConfigs.CONTENT_TYPE_YML)
                               .accept(TestConfigs.CONTENT_TYPE_YML)
                               .body(user, objectMapper)
                               .when().post().then()
                               .statusCode(200).extract().body()
                               .as(TokenVO.class, objectMapper)
                               .getAccessToken();

      specification = new RequestSpecBuilder()
                          .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                          .setBasePath("/person/v1")
                          .setPort(TestConfigs.SERVER_PORT)
                          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                          .build();
   }
   @Test
   @Order(1)
   public void testCreate() throws IOException {
      mockPerson();

      var persistedPerson = given()
                          .spec(specification)
                          .config(RestAssuredConfig.config()
                                  .encoderConfig(EncoderConfig.encoderConfig()
                                          .encodeContentTypeAs(
                                                  TestConfigs.CONTENT_TYPE_YML,
                                                  ContentType.TEXT
                                          )))
                          .contentType(TestConfigs.CONTENT_TYPE_YML)
                          .accept(TestConfigs.CONTENT_TYPE_YML)
                          .body(person, objectMapper)
                          .when().post().then()
                          .statusCode(201).extract().body().as(PersonVO.class, objectMapper);

      person = persistedPerson;

      assertNotNull(persistedPerson);
      assertNotNull(persistedPerson.getId());
      assertNotNull(persistedPerson.getGender());
      assertNotNull(persistedPerson.getAddress());
      assertNotNull(persistedPerson.getFirstName());
      assertNotNull(persistedPerson.getLastName());
      assertTrue(persistedPerson.getEnabled());

      assertTrue(persistedPerson.getId() > 0);

      assertEquals("Nelson", persistedPerson.getFirstName());
      assertEquals("Piquet", persistedPerson.getLastName());
      assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
      assertEquals("Male", persistedPerson.getGender());
   }
   @Test
   @Order(2)
   public void testUpdate() throws IOException {
      person.setLastName("Piquet Souto Maior");

      var persistedPerson = given()
                          .spec(specification)
                          .config(RestAssuredConfig.config()
                                  .encoderConfig(EncoderConfig.encoderConfig()
                                          .encodeContentTypeAs(
                                                  TestConfigs.CONTENT_TYPE_YML,
                                                  ContentType.TEXT
                                          )))
                          .contentType(TestConfigs.CONTENT_TYPE_YML)
                          .accept(TestConfigs.CONTENT_TYPE_YML)
                          .body(person, objectMapper)
                          .when().post().then()
                          .statusCode(201).extract().body().as(PersonVO.class, objectMapper);

      person = persistedPerson;

      assertNotNull(persistedPerson);
      assertNotNull(persistedPerson.getId());
      assertNotNull(persistedPerson.getGender());
      assertNotNull(persistedPerson.getAddress());
      assertNotNull(persistedPerson.getFirstName());
      assertNotNull(persistedPerson.getLastName());
      assertTrue(persistedPerson.getEnabled());


      assertEquals(person.getId(), persistedPerson.getId());

      assertEquals("Nelson", persistedPerson.getFirstName());
      assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
      assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
      assertEquals("Male", persistedPerson.getGender());
   }


   @Test
   @Order(3)
   public void testDisablePerson() throws IOException {

      var persistedPerson = given()
              .spec(specification)
              .config(RestAssuredConfig.config()
                      .encoderConfig(EncoderConfig.encoderConfig()
                              .encodeContentTypeAs(
                                      TestConfigs.CONTENT_TYPE_YML,
                                      ContentType.TEXT
                              )))
              .contentType(TestConfigs.CONTENT_TYPE_YML)
              .accept(TestConfigs.CONTENT_TYPE_YML)
              .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
              .pathParam("id", person.getId())
              .when().patch("{id}").then()
              .statusCode(200).extract().body().as(PersonVO.class, objectMapper);

      person = persistedPerson;

      assertNotNull(persistedPerson);
      assertNotNull(persistedPerson.getId());
      assertNotNull(persistedPerson.getGender());
      assertNotNull(persistedPerson.getAddress());
      assertNotNull(persistedPerson.getFirstName());
      assertNotNull(persistedPerson.getLastName());
      assertFalse(persistedPerson.getEnabled());


      assertEquals(person.getId(), persistedPerson.getId());

      assertEquals("Nelson", persistedPerson.getFirstName());
      assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
      assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
      assertEquals("Male", persistedPerson.getGender());
   }

   @Test
   @Order(4)
   public void testFindById() throws IOException {
      mockPerson();

      var persistedPerson = given()
                          .spec(specification)
                          .config(RestAssuredConfig.config()
                                  .encoderConfig(EncoderConfig.encoderConfig()
                                          .encodeContentTypeAs(
                                                  TestConfigs.CONTENT_TYPE_YML,
                                                  ContentType.TEXT
                                          )))
                          .contentType(TestConfigs.CONTENT_TYPE_YML)
                          .accept(TestConfigs.CONTENT_TYPE_YML)
                          .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                          .pathParam("id", person.getId())
                          .when().get("{id}").then()
                          .statusCode(200).extract().body().as(PersonVO.class, objectMapper);

      person = persistedPerson;

      assertNotNull(persistedPerson);
      assertNotNull(persistedPerson.getId());
      assertNotNull(persistedPerson.getGender());
      assertNotNull(persistedPerson.getAddress());
      assertNotNull(persistedPerson.getFirstName());
      assertNotNull(persistedPerson.getLastName());
      assertFalse(persistedPerson.getEnabled());

      assertEquals(person.getId(), persistedPerson.getId());

      assertEquals("Nelson", persistedPerson.getFirstName());
      assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
      assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
      assertEquals("Male", persistedPerson.getGender());
   }

   @Test
   @Order(5)
   public void testDelete() throws IOException {
      mockPerson();

      given().spec(specification)
             .config(RestAssuredConfig.config()
                     .encoderConfig(EncoderConfig.encoderConfig()
                             .encodeContentTypeAs(
                                     TestConfigs.CONTENT_TYPE_YML,
                                     ContentType.TEXT
                             )))
             .contentType(TestConfigs.CONTENT_TYPE_YML)
             .accept(TestConfigs.CONTENT_TYPE_YML)
             .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
             .pathParam("id", person.getId())
             .when().delete("{id}")
             .then()
             .statusCode(204);
   }

   @Test
   @Order(6)
   public void testFindAll() throws IOException {
      mockPerson();

      var wrapper = given()
                                   .spec(specification)
                                   .config(RestAssuredConfig.config()
                                           .encoderConfig(EncoderConfig.encoderConfig()
                                                   .encodeContentTypeAs(
                                                           TestConfigs.CONTENT_TYPE_YML,
                                                           ContentType.TEXT
                                                   )))
                                   .contentType(TestConfigs.CONTENT_TYPE_YML)
                                   .accept(TestConfigs.CONTENT_TYPE_YML)
                                   .body(person, objectMapper)
                                   .when().get().then()
                                   .statusCode(200)
                                   .extract().body().as(WrapperPersonVO.class, objectMapper);

      var people = wrapper.getEmbedded().getPersons();

      PersonVO foundPersonOne = people.get(0);

      assertNotNull(foundPersonOne.getId());
      assertNotNull(foundPersonOne.getGender());
      assertNotNull(foundPersonOne.getAddress());
      assertNotNull(foundPersonOne.getFirstName());
      assertNotNull(foundPersonOne.getLastName());
      assertTrue(foundPersonOne.getEnabled());

      assertEquals(1, foundPersonOne.getId());

      assertEquals("Ayrton", foundPersonOne.getFirstName());
      assertEquals("Senna", foundPersonOne.getLastName());
      assertEquals("São Paulo", foundPersonOne.getAddress());
      assertEquals("Male", foundPersonOne.getGender());

      PersonVO foundPersonSix = people.get(5);

      assertNotNull(foundPersonSix.getId());
      assertNotNull(foundPersonSix.getGender());
      assertNotNull(foundPersonSix.getAddress());
      assertNotNull(foundPersonSix.getFirstName());
      assertNotNull(foundPersonSix.getLastName());
      assertTrue(foundPersonSix.getEnabled());

      assertEquals(9, foundPersonSix.getId());

      assertEquals("Nelson", foundPersonSix.getFirstName());
      assertEquals("Mvezo", foundPersonSix.getLastName());
      assertEquals("Mvezo – South Africa", foundPersonSix.getAddress());
      assertEquals("Male", foundPersonSix.getGender());
   }

   @Test
   @Order(7)
   public void testFindAllWithoutToken() throws IOException {
      mockPerson();

      RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                                                           .setBasePath("/person/v1")
                                                           .setPort(TestConfigs.SERVER_PORT)
                                                           .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                                           .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                                           .build();

      given().spec(specificationWithoutToken)
             .config(RestAssuredConfig.config()
                     .encoderConfig(EncoderConfig.encoderConfig()
                             .encodeContentTypeAs(
                                     TestConfigs.CONTENT_TYPE_YML,
                                     ContentType.TEXT
                             )))
              .contentType(TestConfigs.CONTENT_TYPE_YML)
              .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
              .accept(TestConfigs.CONTENT_TYPE_YML)
              .pathParam("id", person.getId())
              .when().delete("{id}")
              .then()
              .statusCode(403);
   }

   private void mockPerson() {
      person.setFirstName("Nelson");
      person.setLastName("Piquet");
      person.setAddress("Brasília - DF - Brasil");
      person.setGender("Male");
      person.setEnabled(true);
   }
}
