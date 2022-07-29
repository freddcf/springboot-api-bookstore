package com.fredfonseca.bookstoremanager.books.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
