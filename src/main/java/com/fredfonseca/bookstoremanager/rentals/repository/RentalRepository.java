package com.fredfonseca.bookstoremanager.rentals.repository;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBookAndUsers(Object book, Object user);

    List<Rental> findByBook(Book book);

    List<Rental> findByUsers(Users user);

    Optional<Rental> findByIdAndUsers(Long id, Users user);

    Page<Rental> findAllByUsers(Users user, Pageable pageable);
}
