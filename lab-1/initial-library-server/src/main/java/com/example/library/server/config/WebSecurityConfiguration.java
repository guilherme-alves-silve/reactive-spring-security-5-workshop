package com.example.library.server.config;

import com.example.library.server.common.Role;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.net.URI;

@EnableWebFluxSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                    .matchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .matchers(EndpointRequest.to("health"))
                    .permitAll()
                    .matchers(EndpointRequest.to("info"))
                    .permitAll()
                    .matchers(EndpointRequest.toAnyEndpoint())
                    .hasRole(Role.LIBRARY_ADMIN.name())
                    .pathMatchers(HttpMethod.POST, "/books/{bookId}/borrow")
                    .hasRole(Role.LIBRARY_USER.name())
                    .pathMatchers(HttpMethod.POST, "/books/{bookId}/return")
                    .hasRole(Role.LIBRARY_USER.name())
                    .pathMatchers(HttpMethod.POST, "/books")
                    .hasRole(Role.LIBRARY_CURATOR.name())
                    .pathMatchers(HttpMethod.DELETE, "/books")
                    .hasRole(Role.LIBRARY_CURATOR.name())
                    .pathMatchers("/users/**")
                    .hasRole(Role.LIBRARY_ADMIN.name())
                    .anyExchange()
                    .authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .formLogin()
                    .and()
                    .logout()
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .and()
                    .build();
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/books"));
        return logoutSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
