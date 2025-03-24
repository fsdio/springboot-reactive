package com.example.springboot_reactive.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_reactive.dto.request.AuthRequest;
import com.example.springboot_reactive.dto.response.AuthResponse;
import com.example.springboot_reactive.repository.UserRepository;
import com.example.springboot_reactive.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@Deprecated
public class AuthController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ReactiveAuthenticationManager authenticationManager;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @PostMapping("/login")
  public Mono<AuthResponse> login(@RequestBody AuthRequest request) {
    String username = request.getUsername();
    Mono<Authentication> authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));

    return authentication
        .flatMap(auth -> {
          String token = jwtTokenProvider.createToken(auth);
          return Mono.just(new AuthResponse(token));
        })
        .onErrorResume(e -> {
          // Handle specific exceptions if needed
          return Mono.error(new RuntimeException("Failed to generate token", e));
        });
  }
}
