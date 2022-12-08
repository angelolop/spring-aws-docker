package com.example.springawsdocker.services;

import com.example.springawsdocker.controllers.PersonController;
import com.example.springawsdocker.exceptions.RequiredObjectIsNullException;
import com.example.springawsdocker.exceptions.ResourceNotFoundException;
import com.example.springawsdocker.mapper.DozerMapper;
import com.example.springawsdocker.mapper.custom.PersonMapper;
import com.example.springawsdocker.models.Person;
import com.example.springawsdocker.repositories.PersonRepository;
import com.example.springawsdocker.vo.v1.PersonVO;
import com.example.springawsdocker.vo.v2.PersonVOV2;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServices {

   @Autowired
   PersonRepository personRepository;

   @Autowired
   PagedResourcesAssembler<PersonVO> assembler;

   @Autowired
   PersonMapper mapperV2;

   public PagedModel<EntityModel<PersonVO>> findAllPerson(Pageable pageable) {
      var personPage = personRepository.findAll(pageable);

      var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

      personVosPage.map(p -> {
                              try {
                                return p.add(linkTo(methodOn(PersonController.class).findPersonById(p.getKey())).withSelfRel());
                              } catch (Exception e) {
                                 throw new RuntimeException(e);
                                 }
                              });

      Link link = linkTo(methodOn(PersonController.class)
                        .findAllPerson(pageable.getPageNumber(),
                                       pageable.getPageSize(),
                               "asc")).withSelfRel();
      return assembler.toModel(personVosPage, link);
   }
   public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
      var personPage = personRepository.findPersonsByName(firstName, pageable);

      var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

      personVosPage.map(p -> {
                              try {
                                return p.add(linkTo(methodOn(PersonController.class).findPersonById(p.getKey())).withSelfRel());
                              } catch (Exception e) {
                                 throw new RuntimeException(e);
                                 }
                              });

      Link link = linkTo(methodOn(PersonController.class)
                        .findAllPerson(pageable.getPageNumber(),
                                       pageable.getPageSize(),
                               "asc")).withSelfRel();
      return assembler.toModel(personVosPage, link);
   }

   public PersonVO findPersonById(Long id) throws Exception {
      PersonVO vo = DozerMapper.parseObject(personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"))
                                    , PersonVO.class);
      vo.add(linkTo(methodOn(PersonController.class).findPersonById(id)).withSelfRel());
      return vo;
   }

   @Transactional
   public PersonVO disablePerson(Long id) throws Exception {
      personRepository.disablePerson(id);

      PersonVO vo = DozerMapper.parseObject(personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"))
                                    , PersonVO.class);
      vo.add(linkTo(methodOn(PersonController.class).findPersonById(id)).withSelfRel());
      return vo;
   }

   public PersonVO savePerson(PersonVO PersonVO) throws Exception {
      if(PersonVO == null) throw new RequiredObjectIsNullException();
      var entity = DozerMapper.parseObject(PersonVO, Person.class);
      Person test = personRepository.save(entity);
      var vo = DozerMapper.parseObject(test, PersonVO.class);
      vo.add(linkTo(methodOn(PersonController.class).findPersonById(vo.getKey())).withSelfRel());
      return vo;
   }

   public PersonVO updatePerson(Long id, PersonVO PersonVOToUpdate) throws Exception {
      if(PersonVOToUpdate == null) throw new RequiredObjectIsNullException();
      var entity = findPersonById(id);
      entity.setFirstName(PersonVOToUpdate.getFirstName());
      entity.setLastName(PersonVOToUpdate.getLastName());
      entity.setAddress(PersonVOToUpdate.getAddress());
      entity.setGender(PersonVOToUpdate.getGender());
      PersonVO vo = DozerMapper.parseObject(DozerMapper.parseObject(PersonVOToUpdate,Person.class), PersonVO.class);
      vo.add(linkTo(methodOn(PersonController.class).findPersonById(vo.getKey())).withSelfRel());
      return vo;
   }

   public void deletePerson(Long id) throws Exception {
      findPersonById(id);
      personRepository.deleteById(id);
   }

   public PersonVOV2 savePersonV2(PersonVOV2 PersonVO) {
      return mapperV2.convertEntityToVo(personRepository.save(DozerMapper.parseObject(PersonVO, Person.class)));
   }
}
