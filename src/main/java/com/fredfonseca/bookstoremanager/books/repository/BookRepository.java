package com.fredfonseca.bookstoremanager.books.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByName(String name);

    Optional<Book> findByPublisher(Publisher publisher);
}
