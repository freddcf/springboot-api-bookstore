package com.fredfonseca.bookstoremanager.rentals.entity;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate rentalDate;

    @Column(nullable = false)
    private LocalDate returnForecast;

    @Column(length = 30)
    private String returnDate;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Book book;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Users users;
}
