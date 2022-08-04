package com.fredfonseca.bookstoremanager.books.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByNameAndAuthor(String name, String author);
}
