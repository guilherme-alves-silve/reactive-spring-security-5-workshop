package com.example.library.server.business;

import com.example.library.server.InitialLibraryServerApplication;
import com.example.library.server.common.Role;
import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.when;

@DisplayName("Verify that user service")
@SpringJUnitConfig(InitialLibraryServerApplication.class)
public class UserServiceAuthorizationTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("grants access to find one user by email for anonymous user")
    @Test
    void shouldVerifyFindOneByEmailAccessIsGrantedForUnauthenticated() {
        when(userRepository.findOneByEmail(anyString()))
                .thenReturn(
                        Mono.just(
                                new User(
                                        UUID.randomUUID(),
                                        "test@example.com",
                                        "secret",
                                        "Max",
                                        "Maier",
                                        Collections.singletonList(Role.LIBRARY_USER)
                                )
                        )
                );
        StepVerifier.create(userRepository.findOneByEmail("test@example.com"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @DisplayName("grants access to find one user by email for roles 'LIBRARY_USER', 'LIBRARY_CURATOR' and 'LIBRARY_ADMIN'")
    @Test
    @WithMockUser(roles = { "LIBRARY_USER", "LIBRARY_CURATOR", "LIBRARY_ADMIN" })
    void shouldVerifyFindOneByEmailAccessIsGrantedForAllRoles() {
        when(userRepository.findOneByEmail(anyString()))
                .thenReturn(
                        Mono.just(
                                new User(
                                        UUID.randomUUID(),
                                        "test@example.com",
                                        "secret",
                                        "Max",
                                        "Maier",
                                        Collections.singletonList(Role.LIBRARY_USER)
                                )
                        )
                );
        StepVerifier.create(userRepository.findOneByEmail("test@example.com"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @DisplayName("denies access to create a user for roles user 'LIBRARY_USER' and 'LIBRARY_CURATOR'")
    @Test
    @WithMockUser(roles = { "LIBRARY_USER", "LIBRARY_CURATOR" })
    void shouldCreateAccessIsDeniedForUserAndCurator() {
        StepVerifier.create(
                userService.create(
                        Mono.just(
                                new User(
                                        UUID.randomUUID(),
                                        "test@example.com",
                                        "secret",
                                        "Max",
                                        "Maier",
                                        Collections.singletonList(Role.LIBRARY_USER)
                                )
                        )
                )
        ).verifyError(AccessDeniedException.class);
    }
}
