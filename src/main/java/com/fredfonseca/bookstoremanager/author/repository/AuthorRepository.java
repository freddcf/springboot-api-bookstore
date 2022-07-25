package com.fredfonseca.bookstoremanager.author.repository;

import com.fredfonseca.bookstoremanager.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
