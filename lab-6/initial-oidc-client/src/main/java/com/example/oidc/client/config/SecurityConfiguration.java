package com.example.oidc.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

  @Bean
  SecurityWebFilterChain configure(ServerHttpSecurity http) {
    http
        .authorizeExchange()
        .anyExchange()
        .authenticated()
        .and()
        .oauth2Login()
        .and()
        .oauth2Client();
    return http.build();
  }
}
