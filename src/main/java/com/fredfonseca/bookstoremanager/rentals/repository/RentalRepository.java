package com.fredfonseca.bookstoremanager.rentals.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBookAndUsers(Object book, Object user);

    Optional<Rental> findByBook(Book book);

    Optional<Rental> findByUsers(Users user);
}
