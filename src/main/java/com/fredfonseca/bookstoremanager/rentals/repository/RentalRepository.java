package com.fredfonseca.bookstoremanager.rentals.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBookAndUsers(Object book, Object user);

    List<Rental> findByBook(Book book);

    List<Rental> findByUsers(Users user);
}
