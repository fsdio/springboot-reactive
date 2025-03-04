package com.example.springboot_reactive.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@RestController
@EnableWebFlux
public class HelloController {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @GetMapping(path = "/hello", produces = "application/json")
    @ResponseBody
    public Mono<String> handle() {
        return Mono.just("Hello WebFlux");
    }

    @GetMapping("/cookie")
    public String handle(@CookieValue("JSESSIONID") String cookie) { 
        return "cookie: " + cookie;
    }

    @GetMapping("/jwt")
    public Mono<Map<String, String>> getJwt() { 
        Map<String, String> data = new HashMap<>();
        data.put("token", generateToken());
        return Mono.just(data);
    }

    @GetMapping("/validate")
    public Mono<String> validateToken(@RequestHeader("Authorization") String token) {
        return Mono.just(validateJwt(token.replace("Bearer ", "")) ? "Valid token" : "Invalid token");
    }

    public String generateToken(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "admin");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject("admin")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(key)
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    public boolean validateJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
