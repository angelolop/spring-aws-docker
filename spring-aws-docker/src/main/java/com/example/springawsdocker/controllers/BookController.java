package com.example.springawsdocker.controllers;

import com.example.springawsdocker.services.BookServices;
import com.example.springawsdocker.util.MediaType;
import com.example.springawsdocker.vo.v1.BookVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/book/v1")
@Tag(name = "Book", description = "Endpoints for managing Book")
public class BookController {

   @Autowired
   private BookServices bookService;

   @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Finds all Book", description = "Finds all Book", tags = {"Book"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content = {
                   @Content(
                           mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                   )}),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public List<BookVO> findAllBook() throws Exception {
      return bookService.findAllBook();
   }

   @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Finds a book", description = "Finds a book", tags = {"Book"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200",
                   content = @Content(schema = @Schema(implementation = BookVO.class))
                   ),
           @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public BookVO findBookById(@PathVariable long id) throws Exception {
      return bookService.findBookById(id);
   }

   @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
   @ResponseStatus(CREATED)
   @Operation(summary = "Adds a new Book", description = "Adds a Book", tags = {"Book"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200",
                   content = @Content(schema = @Schema(implementation = BookVO.class))
           ),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public BookVO saveBook(@RequestBody BookVO bookVO) throws Exception {
      return bookService.saveBook(bookVO);
   }

   @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
   @ResponseStatus(CREATED)
   @Operation(summary = "Updates a Book", description = "Updates a Book", tags = {"Book"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200",
                   content = @Content(schema = @Schema(implementation = BookVO.class))
           ),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public BookVO saveBook(@PathVariable long id, @RequestBody BookVO bookVO) throws Exception {
      return bookService.updateBook(id, bookVO);
   }

   @DeleteMapping
   @ResponseStatus(NO_CONTENT)
   @Operation(summary = "Deletes a book", description = "Deletes a book", tags = {"Book"}, responses = {
           @ApiResponse(description = "No content", responseCode = "204", content = @Content),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public void deleteBook(@PathVariable long id) throws Exception {
      bookService.deleteBook(id);
   }
}