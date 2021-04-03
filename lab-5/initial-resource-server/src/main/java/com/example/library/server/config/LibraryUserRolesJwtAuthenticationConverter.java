package com.example.library.server.config;

import com.example.library.server.security.LibraryReactiveUserDetailsService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public class LibraryUserRolesJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final LibraryReactiveUserDetailsService libraryReactiveUserDetailsService;

    public LibraryUserRolesJwtAuthenticationConverter(LibraryReactiveUserDetailsService libraryReactiveUserDetailsService) {
        this.libraryReactiveUserDetailsService = libraryReactiveUserDetailsService;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return libraryReactiveUserDetailsService
                .findByUsername(jwt.getClaimAsString("email"))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, "n/a", userDetails.getAuthorities()));
    }
}
