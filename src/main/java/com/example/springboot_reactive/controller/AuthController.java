package com.example.springboot_reactive.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_reactive.dto.LoginDto;
import com.github.f4b6a3.uuid.UuidCreator;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Map<String, Object>> postData(@RequestBody Map<String, String> data) {
		UUID uuid = UuidCreator.getTimeOrderedEpoch();
		Map<String, Object> response = new HashMap<>();
		response.put("receivedData", data);
		response.put("timestamp", uuid);
		return Mono.just(response);
	}

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Map<String, Object>> login(@RequestBody LoginDto data) {

		Map<String, PasswordEncoder> idToPasswordEncoder = new HashMap<>();
		String idForEncode = "bcrypt";
		idToPasswordEncoder.put(idForEncode, new BCryptPasswordEncoder(17));
		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(
				idForEncode,
				idToPasswordEncoder);
		UUID uuid = UuidCreator.getTimeOrderedEpoch();
		data.setPassword(passwordEncoder.encode(CharSequence.class.cast(data.getPassword())));
		Map<String, Object> response = new HashMap<>();
		response.put("receivedData", data);
		response.put("timestamp", uuid);
		return Mono.just(response);
	}
}
