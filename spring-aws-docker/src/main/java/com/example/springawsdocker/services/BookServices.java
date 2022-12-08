package com.example.springawsdocker.services;

import com.example.springawsdocker.controllers.BookController;
import com.example.springawsdocker.exceptions.RequiredObjectIsNullException;
import com.example.springawsdocker.exceptions.ResourceNotFoundException;
import com.example.springawsdocker.mapper.DozerMapper;
import com.example.springawsdocker.models.Book;
import com.example.springawsdocker.repositories.BookRepository;
import com.example.springawsdocker.vo.v1.BookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

   @Autowired
   BookRepository bookRepository;

   public List<BookVO> findAllBook() {
      var books = DozerMapper.parseListObjects(bookRepository.findAll(), BookVO.class) ;
      books.forEach(p -> {
         try {
            p.add(linkTo(methodOn(BookController.class).findBookById(p.getKey())).withSelfRel());
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      });
      return books;
   }

   public BookVO findBookById(Long id) throws Exception {
      BookVO vo = DozerMapper.parseObject(bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"))
                                    , BookVO.class);
      vo.add(linkTo(methodOn(BookController.class).findBookById(id)).withSelfRel());
      return vo;
   }

   public BookVO saveBook(BookVO BookVo) throws Exception {
      if(BookVo == null) throw new RequiredObjectIsNullException();
      var entity = DozerMapper.parseObject(BookVo, Book.class);
      Book test = bookRepository.save(entity);
      var vo = DozerMapper.parseObject(test, BookVO.class);
      vo.add(linkTo(methodOn(BookController.class).findBookById(vo.getKey())).withSelfRel());
      return vo;
   }

   public BookVO updateBook(Long id, BookVO BookVOToUpdate) throws Exception {
      if(BookVOToUpdate == null) throw new RequiredObjectIsNullException();
      var entity = findBookById(id);
      entity.setAuthor(BookVOToUpdate.getAuthor());
      entity.setLaunchDate(BookVOToUpdate.getLaunchDate());
      entity.setPrice(BookVOToUpdate.getPrice());
      entity.setTitle(BookVOToUpdate.getTitle());
      BookVO vo = DozerMapper.parseObject(DozerMapper.parseObject(BookVOToUpdate,Book.class), BookVO.class);
      vo.add(linkTo(methodOn(BookController.class).findBookById(vo.getKey())).withSelfRel());
      return vo;
   }

   public void deleteBook(Long id) throws Exception {
      findBookById(id);
      bookRepository.deleteById(id);
   }
}
