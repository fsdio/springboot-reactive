package com.example.springboot_reactive.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springboot_reactive.dto.request.RegisterRequest;
import com.example.springboot_reactive.models.User;
import com.example.springboot_reactive.repository.UserRepository;
import com.github.f4b6a3.uuid.UuidCreator;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public Mono<User> createUser(RegisterRequest user) {
    User newUser = new User();
    newUser.setId(String.valueOf((UUID) UuidCreator.getTimeOrderedEpoch()));
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setRoles(user.getRoles());
    return userRepository.save(newUser);
  }

}
