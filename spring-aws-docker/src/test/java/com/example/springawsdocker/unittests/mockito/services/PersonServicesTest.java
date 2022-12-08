package com.example.springawsdocker.unittests.mockito.services;

import com.example.springawsdocker.exceptions.RequiredObjectIsNullException;
import com.example.springawsdocker.models.Person;
import com.example.springawsdocker.repositories.PersonRepository;
import com.example.springawsdocker.services.PersonServices;
import com.example.springawsdocker.mapper.mocks.MockPerson;
import com.example.springawsdocker.vo.v1.PersonVO;
import com.example.springawsdocker.models.Person;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

   MockPerson input;

   @InjectMocks
   private PersonServices service;

   @Mock
   PersonRepository personRepository;

   @BeforeEach
   void setUpMocks() throws Exception{
      input = new MockPerson();
      MockitoAnnotations.openMocks(this);
   }

   @Test
   void findPersonById() throws Exception {
      Person entity = input.mockEntity(1);
      entity.setId(1L);

      when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
      var result = service.findPersonById(1L);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</person/v1/1>;rel=\"self\"]"));
      assertEquals("Address Test1", result.getAddress());
      assertEquals("First Name Test1", result.getFirstName());
      assertEquals("Last Name Test1", result.getLastName());
      assertEquals("Female", result.getGender());

   }

//   @Test
//   @Ignore
//   void findAllPerson() {
//      List<Person> list = input.mockEntityList();
//      when(personRepository.findAll()).thenReturn(list);
//      var people = service.findAllPerson(pageable);
//      assertNotNull(people);
//      assertEquals(14, people.size());
//
//      var personOne = people.get(1);
//      assertNotNull(personOne);
//      assertNotNull(personOne.getKey());
//      assertNotNull(personOne.getLinks());
//      assertTrue(personOne.toString().contains("links: [</person/v1/1>;rel=\"self\"]"));
//      assertEquals("Address Test1", personOne.getAddress());
//      assertEquals("First Name Test1", personOne.getFirstName());
//      assertEquals("Last Name Test1", personOne.getLastName());
//      assertEquals("Female", personOne.getGender());
//
//      var personFour = people.get(4);
//      assertNotNull(personFour);
//      assertNotNull(personFour.getKey());
//      assertNotNull(personFour.getLinks());
//      assertTrue(personFour.toString().contains("links: [</person/v1/4>;rel=\"self\"]"));
//      assertEquals("Address Test4", personFour.getAddress());
//      assertEquals("First Name Test4", personFour.getFirstName());
//      assertEquals("Last Name Test4", personFour.getLastName());
//      assertEquals("Male", personFour.getGender());
//
//      var personSeven = people.get(7);
//      assertNotNull(personSeven);
//      assertNotNull(personSeven.getKey());
//      assertNotNull(personSeven.getLinks());
//      assertTrue(personSeven.toString().contains("links: [</person/v1/7>;rel=\"self\"]"));
//      assertEquals("Address Test7", personSeven.getAddress());
//      assertEquals("First Name Test7", personSeven.getFirstName());
//      assertEquals("Last Name Test7", personSeven.getLastName());
//      assertEquals("Female", personSeven.getGender());
//   }

   @Test
   void savePerson() throws Exception {
      Person entity = input.mockEntity(2);

      Person persisted = entity;
      persisted.setId(2L);

      PersonVO vo = input.mockVO(2);
      vo.setKey(2L);

      when(personRepository.save(entity)).thenReturn(persisted);

      PersonVO result = service.savePerson(vo);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</person/v1/2>;rel=\"self\"]"));
      assertEquals("Address Test2", result.getAddress());
      assertEquals("First Name Test2", result.getFirstName());
      assertEquals("Last Name Test2", result.getLastName());
      assertEquals("Male", result.getGender());
   }
   @Test
   void testCreateWithNullPerson() throws Exception {
      Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
         service.savePerson(null);
      });

      String expectedMessage = "It is not allowed to persist a null object";
      String actualMessage = exception.getMessage();
      assertTrue(actualMessage.contains(expectedMessage));
   }
   @Test
   void updateWithNullPerson() throws Exception {
      Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
         service.updatePerson(null, null);
      });

      String expectedMessage = "It is not allowed to persist a null object";
      String actualMessage = exception.getMessage();
      assertTrue(actualMessage.contains(expectedMessage));
   }

   @Test
   void updatePerson() throws Exception {
      Person entity = input.mockEntity(1);
      entity.setId(2L);

      Person persisted = entity;
      persisted.setId(2L);

      PersonVO vo = input.mockVO(2);
      vo.setKey(2L);

      lenient().when(personRepository.findById(2L)).thenReturn(Optional.of(entity));
      lenient().when(personRepository.save(entity)).thenReturn(persisted);

      PersonVO result = service.updatePerson(2L,vo);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</person/v1/2>;rel=\"self\"]"));
      assertEquals("Address Test2", result.getAddress());
      assertEquals("First Name Test2", result.getFirstName());
      assertEquals("Last Name Test2", result.getLastName());
      assertEquals("Male", result.getGender());
   }

   @Test
   void deletePerson() throws Exception {
      Person entity = input.mockEntity(1);
      entity.setId(1L);

      when(personRepository.findById(1L)).thenReturn(Optional.of(entity));
      service.deletePerson(1L);
   }

   @Test
   void savePersonV2() {
   }
}