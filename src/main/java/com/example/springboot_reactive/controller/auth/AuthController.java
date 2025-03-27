package com.example.springboot_reactive.controller.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_reactive.dto.request.AuthRequest;
import com.example.springboot_reactive.dto.request.RegisterRequest;
import com.example.springboot_reactive.dto.response.AuthResponse;
import com.example.springboot_reactive.dto.response.RegisterResponse;
import com.example.springboot_reactive.repository.UserRepository;
import com.example.springboot_reactive.security.jwt.JwtTokenProvider;
import com.example.springboot_reactive.service.TokenService;
import com.example.springboot_reactive.service.UserService;
import com.github.f4b6a3.uuid.UuidCreator;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@Deprecated
public class AuthController {
  @Autowired
  UserService userService;
  @Autowired
  TokenService tokenService;
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
          UUID refreshToken = UuidCreator.getTimeOrderedEpoch();
          String token = jwtTokenProvider.createToken(auth);
          return Mono.just(new AuthResponse(token, String.valueOf(refreshToken)));
        })
        .onErrorResume(e -> {
          return Mono.error(new RuntimeException("Failed to generate token", e));
        });
  }

  @PostMapping("/register")
  public Mono<ResponseEntity<RegisterResponse>> register(@RequestBody RegisterRequest newUser) {
    return userService.createUser(newUser)
        .flatMap(savedUser -> {
          RegisterResponse response = new RegisterResponse(
              "New user created",
              savedUser);
          return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response));
        })
        .onErrorResume(exception -> {
          RegisterResponse response = new RegisterResponse(
              newUser.getUsername() + " sudah digunakan",
              null);
          return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
        });
  }

  @PostMapping("/logout")
  public Mono<String> logout(@RequestParam String username) {
    return Mono.just(username);
  }

  @PostMapping("/create-token")
  public Mono<String> postMethodName(@RequestBody String devices) {
    return tokenService.createRefreshToken(devices);
  }

}
