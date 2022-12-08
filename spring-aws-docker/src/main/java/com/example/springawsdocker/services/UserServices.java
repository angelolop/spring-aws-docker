package com.example.springawsdocker.services;

import com.example.springawsdocker.controllers.PersonController;
import com.example.springawsdocker.exceptions.ResourceNotFoundException;
import com.example.springawsdocker.mapper.DozerMapper;
import com.example.springawsdocker.repositories.UserRepository;
import com.example.springawsdocker.vo.v1.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

   @Autowired
   UserRepository userRepository;

   public UserServices(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public PersonVO findPersonById(Long id) throws Exception {
      PersonVO vo = DozerMapper.parseObject(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"))
                                    , PersonVO.class);
      vo.add(linkTo(methodOn(PersonController.class).findPersonById(id)).withSelfRel());
      return vo;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      var user = userRepository.findByUsername(username);
      if (user != null){
         return user;
      } else {
         throw new UsernameNotFoundException("Username " + username + " not found!");
      }
   }
}
