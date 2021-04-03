package com.example.library.server.business;

import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookRepository;
import com.example.library.server.dataaccess.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@PreAuthorize("hasAnyRole('LIBRARY_USER', 'LIBRARY_CURATOR')")
public class BookService {

  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  @Autowired
  public BookService(final BookRepository bookRepository,
                     final UserRepository userRepository) {
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public Mono<Void> create(Mono<Book> book) {
    return bookRepository.insert(book).then();
  }

  public Mono<Book> findById(UUID uuid) {
    return bookRepository.findById(uuid);
  }

  @PreAuthorize("hasRole('LIBRARY_USER')")
  public Mono<Void> borrowById(UUID bookIdentifier, UUID userIdentifier) {

    if (bookIdentifier == null || userIdentifier == null) {
      return Mono.empty();
    }

    return bookRepository
        .findById(bookIdentifier)
        .log()
        .flatMap(
            book ->
                userRepository
                    .findById(userIdentifier)
                    .flatMap(
                        user -> {
                          book.doBorrow(user);
                          return bookRepository.save(book).then();
                        })
                    .switchIfEmpty(Mono.empty()))
        .switchIfEmpty(Mono.empty());
  }

  @PreAuthorize("hasRole('LIBRARY_USER')")
  public Mono<Void> returnById(UUID bookIdentifier, UUID userIdentifier) {

    if (bookIdentifier == null || userIdentifier == null) {
      return Mono.empty();
    }

    return bookRepository
        .findById(bookIdentifier)
        .log()
        .flatMap(
            book ->
                userRepository
                    .findById(userIdentifier)
                    .flatMap(
                        user -> {
                          book.doReturn(user);
                          return bookRepository.save(book).then();
                        })
                    .switchIfEmpty(Mono.empty()))
        .switchIfEmpty(Mono.empty());
  }

  public Flux<Book> findAll() {
    return bookRepository.findAll();
  }

  @PreAuthorize("hasRole('LIBRARY_CURATOR')")
  public Mono<Void> deleteById(UUID bookIdentifier) {
    return bookRepository.deleteById(bookIdentifier).then();
  }
}
