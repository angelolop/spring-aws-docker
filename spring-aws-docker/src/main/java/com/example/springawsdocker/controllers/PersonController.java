package com.example.springawsdocker.controllers;

import com.example.springawsdocker.services.PersonServices;
import com.example.springawsdocker.util.MediaType;
import com.example.springawsdocker.vo.v1.PersonVO;
import com.example.springawsdocker.vo.v2.PersonVOV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@RequestMapping("/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController {

   @Autowired
   private PersonServices personService;

   @GetMapping(produces = {MediaType.APPLICATION_JSON,
                           MediaType.APPLICATION_XML,
                           MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Finds all people", description = "Finds all people", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content = {
                                                                       @Content(
                                                                                 mediaType = "application/json",
                                                                                 array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                                                         )}),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PagedModel<EntityModel<PersonVO>> findAllPerson(@RequestParam (value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam (value = "limit", defaultValue = "12") Integer limit,
                                                          @RequestParam (value = "direction", defaultValue = "asc") String direction) {

      var sortDirection = "desc".equalsIgnoreCase(direction)
                                    ? Sort.Direction.DESC : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
      return personService.findAllPerson(pageable);
   }
   @GetMapping(value = "/findPersonsByName/{firstName}",
               produces = {MediaType.APPLICATION_JSON,
                           MediaType.APPLICATION_XML,
                           MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Finds people by name", description = "Finds all people", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content = {
                                                                       @Content(
                                                                                 mediaType = "application/json",
                                                                                 array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                                                         )}),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PagedModel<EntityModel<PersonVO>> findPersonsByName(@PathVariable (value = "firstName") String firstName,
                                                              @RequestParam (value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam (value = "limit", defaultValue = "12") Integer limit,
                                                              @RequestParam (value = "direction", defaultValue = "asc") String direction) {

      var sortDirection = "desc".equalsIgnoreCase(direction)
                                    ? Sort.Direction.DESC : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
      return personService.findPersonsByName(firstName, pageable);
   }

   @CrossOrigin(origins = "http://localhost:8081")
   @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_XML,
                                            MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Finds a person", description = "Finds a person", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content =
                                                                       @Content(schema = @Schema(implementation = PersonVO.class))),
           @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PersonVO findPersonById(@PathVariable long id) throws Exception {
      return personService.findPersonById(id);
   }

   @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_XML,
                                            MediaType.APPLICATION_YML})
   @ResponseStatus(OK)
   @Operation(summary = "Disable a specific Person by id", description = "Finds a person", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content =
                                                                       @Content(schema = @Schema(implementation = PersonVO.class))),
           @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PersonVO disablePerson(@PathVariable long id) throws Exception {
      return personService.disablePerson(id);
   }

   @CrossOrigin(origins = {"http://localhost:8081", "https://erudio.com.br"})
   @PostMapping(consumes = {MediaType.APPLICATION_JSON,
                            MediaType.APPLICATION_XML,
                            MediaType.APPLICATION_YML},
                produces = {MediaType.APPLICATION_JSON,
                            MediaType.APPLICATION_XML,
                            MediaType.APPLICATION_YML})
   @ResponseStatus(CREATED)
   @Operation(summary = "Adds a new person", description = "Adds a person", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                                                                                 schema = @Schema(implementation = PersonVO.class))),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PersonVO savePerson(@RequestBody PersonVO personVO) throws Exception {
      return personService.savePerson(personVO);
   }

   @PostMapping(value = "/v2", consumes = {MediaType.APPLICATION_JSON,
                                           MediaType.APPLICATION_XML,
                                           MediaType.APPLICATION_YML},
                               produces = {MediaType.APPLICATION_JSON,
                                           MediaType.APPLICATION_XML,
                                           MediaType.APPLICATION_YML})
   @ResponseStatus(CREATED)
   public PersonVOV2 savePersonV2(@RequestBody PersonVOV2 personVO){
      return personService.savePersonV2(personVO);
   }

   @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_XML,
                                            MediaType.APPLICATION_YML},
                                produces = {MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_XML,
                                            MediaType.APPLICATION_YML})
   @ResponseStatus(CREATED)
   @Operation(summary = "Updates a person", description = "Updates a person", tags = {"People"}, responses = {
           @ApiResponse(description = "Success", responseCode = "200",
                   content = @Content(schema = @Schema(implementation = PersonVO.class))
           ),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public PersonVO savePerson(@PathVariable long id, @RequestBody PersonVO personVO) throws Exception {
      return personService.updatePerson(id, personVO);
   }

   @DeleteMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON,
                                               MediaType.APPLICATION_XML,
                                               MediaType.APPLICATION_YML})
   @ResponseStatus(NO_CONTENT)
   @Operation(summary = "Deletes a person", description = "Deletes a person", tags = {"People"}, responses = {
           @ApiResponse(description = "No content", responseCode = "204", content = @Content),
           @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
           @ApiResponse(description = "Unathorized", responseCode = "401", content = @Content),
           @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
           @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
   })
   public void deletePerson(@PathVariable long id) throws Exception {
      personService.deletePerson(id);
   }
}