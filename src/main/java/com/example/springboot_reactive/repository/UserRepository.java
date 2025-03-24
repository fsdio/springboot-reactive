package com.example.springboot_reactive.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springboot_reactive.models.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
  Mono<UserDetails> findByUsername(String username);
}
