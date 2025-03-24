package com.example.springboot_reactive.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.springboot_reactive.repository.UserRepository;

import reactor.core.publisher.Mono;

@Component
public class CustomUserDetailsService implements ReactiveUserDetailsService {

  private UserRepository users;

  public CustomUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return users.findByUsername(username)
        .switchIfEmpty(Mono.error(() -> new UsernameNotFoundException("Username: " + username + " not found")))
        .cast(UserDetails.class);
  }
}
