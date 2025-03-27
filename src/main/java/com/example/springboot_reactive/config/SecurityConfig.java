package com.example.springboot_reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.example.springboot_reactive.security.jwt.JwtTokenAuthenticationFilter;
import com.example.springboot_reactive.security.jwt.JwtTokenProvider;
import com.example.springboot_reactive.service.CustomUserDetailsService;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
      JwtTokenProvider tokenProvider,
      ReactiveAuthenticationManager reactiveAuthenticationManager) {

    DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
        new SecurityContextServerLogoutHandler(), new WebSessionServerLogoutHandler());

    http
        .csrf(csrf -> csrf.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .authenticationManager(reactiveAuthenticationManager)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint((swe, e) -> {
              swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
              return swe.getResponse()
                  .writeWith(Mono.just(new DefaultDataBufferFactory().wrap("UNAUTHORIZED".getBytes())));
            })
            .accessDeniedHandler((swe, e) -> {
              swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
              return swe.getResponse()
                  .writeWith(Mono.just(new DefaultDataBufferFactory().wrap("FORBIDDEN".getBytes())));
            }))
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(
            authorizeExchange -> authorizeExchange
                .pathMatchers(HttpMethod.POST,
                    "/api/v1/auth/**",
                    "/actuator/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/user").hasRole("USER")
                .anyExchange().permitAll())
        .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
        .logout(logout -> logout.logoutHandler(logoutHandler));

    return http.build();
  }

  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager(
      CustomUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
        userDetailsService);
    authenticationManager.setPasswordEncoder(passwordEncoder);
    return authenticationManager;
  }
}
