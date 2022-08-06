package com.fredfonseca.bookstoremanager.rentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
