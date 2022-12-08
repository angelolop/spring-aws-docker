package com.example.springawsdocker.unittests.mockito.services;

import com.example.springawsdocker.exceptions.RequiredObjectIsNullException;
import com.example.springawsdocker.mapper.mocks.MockBook;
import com.example.springawsdocker.models.Book;
import com.example.springawsdocker.repositories.BookRepository;
import com.example.springawsdocker.services.BookServices;
import com.example.springawsdocker.vo.v1.BookVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

   MockBook input;

   @InjectMocks
   private BookServices service;

   @Mock
   BookRepository bookRepository;

   @BeforeEach
   void setUpMocks() throws Exception{
      input = new MockBook();
      MockitoAnnotations.openMocks(this);
   }

   @Test
   void findBookById() throws Exception {
      Book entity = input.mockEntity(1);
      entity.setId(1L);

      when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
      var result = service.findBookById(1L);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</book/v1/1>;rel=\"self\"]"));
      assertEquals("Some Author1", result.getAuthor());
      assertEquals("Some Title1", result.getTitle());
      assertEquals(25D, result.getPrice());
      assertNotNull(result.getLaunchDate());

   }

   @Test
   void findAllBook() {
      List<Book> list = input.mockEntityList();
      when(bookRepository.findAll()).thenReturn(list);
      var people = service.findAllBook();
      assertNotNull(people);
      assertEquals(14, people.size());

      var bookOne = people.get(1);
      assertNotNull(bookOne);
      assertNotNull(bookOne.getKey());
      assertNotNull(bookOne.getLinks());
      assertTrue(bookOne.toString().contains("links: [</book/v1/1>;rel=\"self\"]"));
      assertEquals("Some Author1", bookOne.getAuthor());
      assertEquals("Some Title1", bookOne.getTitle());
      assertEquals(25D, bookOne.getPrice());
      assertNotNull(bookOne.getLaunchDate());

      var bookFour = people.get(4);
      assertNotNull(bookFour);
      assertNotNull(bookFour.getKey());
      assertNotNull(bookFour.getLinks());
      assertTrue(bookFour.toString().contains("links: [</book/v1/4>;rel=\"self\"]"));
      assertEquals("Some Author4", bookFour.getAuthor());
      assertEquals("Some Title4", bookFour.getTitle());
      assertEquals(25D, bookFour.getPrice());
      assertNotNull(bookFour.getLaunchDate());

      var bookSeven = people.get(7);
      assertNotNull(bookSeven);
      assertNotNull(bookSeven.getKey());
      assertNotNull(bookSeven.getLinks());
      assertTrue(bookSeven.toString().contains("links: [</book/v1/7>;rel=\"self\"]"));
      assertEquals("Some Author7", bookSeven.getAuthor());
      assertEquals("Some Title7", bookSeven.getTitle());
      assertEquals(25D, bookSeven.getPrice());
      assertNotNull(bookSeven.getLaunchDate());
   }

   /*@Test
   void saveBook() throws Exception {
      Book entity = input.mockEntity(2);

      Book persisted = entity;
      persisted.setId(2L);

      BookVO vo = input.mockVO(2);
      vo.setKey(2L);

      lenient().when(bookRepository.save(entity)).thenReturn(persisted);

      BookVO result = service.saveBook(vo);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</book/v1/2>;rel=\"self\"]"));
      assertEquals("Some Author2", result.getAuthor());
      assertEquals("Some Title2", result.getTitle());
      assertEquals(25D, result.getPrice());
      assertNotNull(result.getLaunchDate());
   }*/
   @Test
   void testCreateWithNullBook() throws Exception {
      Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
         service.saveBook(null);
      });

      String expectedMessage = "It is not allowed to persist a null object";
      String actualMessage = exception.getMessage();
      assertTrue(actualMessage.contains(expectedMessage));
   }
   @Test
   void updateWithNullBook() throws Exception {
      Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
         service.updateBook(null, null);
      });

      String expectedMessage = "It is not allowed to persist a null object";
      String actualMessage = exception.getMessage();
      assertTrue(actualMessage.contains(expectedMessage));
   }

   @Test
   void updateBook() throws Exception {
      Book entity = input.mockEntity(1);
      entity.setId(2L);

      Book persisted = entity;
      persisted.setId(2L);

      BookVO vo = input.mockVO(2);
      vo.setKey(2L);

      lenient().when(bookRepository.findById(2L)).thenReturn(Optional.of(entity));
      lenient().when(bookRepository.save(entity)).thenReturn(persisted);

      BookVO result = service.updateBook(2L,vo);
      assertNotNull(result);
      assertNotNull(result.getKey());
      assertNotNull(result.getLinks());
      assertTrue(result.toString().contains("links: [</book/v1/2>;rel=\"self\"]"));
      assertEquals("Some Author2", result.getAuthor());
      assertEquals("Some Title2", result.getTitle());
      assertEquals(25D, result.getPrice());
      assertNotNull(result.getLaunchDate());
   }

   @Test
   void deleteBook() throws Exception {
      Book entity = input.mockEntity(1);
      entity.setId(1L);

      when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
      service.deleteBook(1L);
   }
}