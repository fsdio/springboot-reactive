package com.example.springboot_reactive.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.springboot_reactive.models.Token;

public interface TokenRepository extends ReactiveCrudRepository<Token, String> {

}
