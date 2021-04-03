package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.dataaccess.BookBuilder;
import com.example.library.server.dataaccess.UserRepository;
import com.example.library.server.testconfig.ApiAuthenticationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ApiAuthenticationTestConfig.class)
public class BookApiAuthenticationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserRepository userRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @DisplayName("as authenticated user is granted")
    @Nested
    class AuthenticatedBookApi {

        @DisplayName("to get list of books")
        @Test
        @WithMockUser
        void shouldVerifyBooksAuthenticated() {

            given(bookService.findAll()).willReturn(Flux.just(BookBuilder.book().build()));

            webTestClient
                    .get()
                    .uri("/books")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectHeader()
                    .exists("X-XSS-Protection")
                    .expectHeader()
                    .valueEquals("X-Frame-Options", "DENY");
        }

        @DisplayName("to get single book")
        @Test
        void shouldVerifyGetBookAuthenticated() {
            UUID bookId = UUID.randomUUID();

            given(bookService.findById(bookId))
                    .willReturn(Mono.just(BookBuilder.book().withId(bookId).build()));

            webTestClient
                    .mutateWith(mockUser())
                    .get()
                    .uri("/books/{bookId}", bookId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk();
        }
    }

    @DisplayName("as unauthenticated user is denied with 401")
    @Nested
    class UnAuthenticatedBookApi {

        @DisplayName("to get list of books")
        @Test
        void shouldVerifyGetBooksUnAuthenticated() {
            webTestClient
                    .get()
                    .uri("/books")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isUnauthorized();
        }
    }
}
