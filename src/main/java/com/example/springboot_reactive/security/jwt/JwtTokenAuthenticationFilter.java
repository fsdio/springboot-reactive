package com.example.springboot_reactive.security.jwt;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Deprecated
public class JwtTokenAuthenticationFilter implements WebFilter {

  public static final String HEADER_PREFIX = "Bearer ";

  private final JwtTokenProvider tokenProvider;

  public JwtTokenAuthenticationFilter(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String token = resolveToken(exchange.getRequest());
    if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
      Authentication authentication = this.tokenProvider.getAuthentication(token);
      return chain.filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
    return chain.filter(exchange);
  }

  private String resolveToken(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    // Check if bearerToken is not null and has the correct prefix
    if (bearerToken != null && StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(7);
    }

    return null;
  }
}
