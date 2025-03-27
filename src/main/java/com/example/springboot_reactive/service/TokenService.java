package com.example.springboot_reactive.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot_reactive.models.Token;
import com.example.springboot_reactive.repository.TokenRepository;
import com.github.f4b6a3.uuid.UuidCreator;

import reactor.core.publisher.Mono;

@Service
public class TokenService {
  @Autowired
  private TokenRepository tokenRepository;

  public Mono<String> createRefreshToken(String devices) {
    Token newToken = new Token();
    newToken.setIdToken(String.valueOf(UUID.randomUUID()));
    newToken.setIdUser(String.valueOf((UUID) UuidCreator.getTimeOrderedEpoch()));
    newToken.setRefreshToken(String.valueOf(UUID.randomUUID()));
    newToken.setDeviceJson(devices);

    return tokenRepository.save(newToken)
        .flatMap(savedUser -> {
          return Mono.just("Berhasil Simpan.");
        })
        .onErrorResume(exception -> {
          return Mono.just(exception.getMessage());
        });
  }
}
