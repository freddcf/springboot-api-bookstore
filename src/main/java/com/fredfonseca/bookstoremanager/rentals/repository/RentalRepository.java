package com.fredfonseca.bookstoremanager.rentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBookAndUsers(Object book, Object user);
}
