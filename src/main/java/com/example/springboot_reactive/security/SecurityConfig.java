package com.example.springboot_reactive.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange(
						authorizeExchange -> authorizeExchange
								.pathMatchers("/api/auth/login")
								.permitAll())
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.builder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();
		return new MapReactiveUserDetailsService(user);
	}
}
