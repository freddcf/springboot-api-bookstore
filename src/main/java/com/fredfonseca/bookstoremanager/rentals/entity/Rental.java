package com.fredfonseca.bookstoremanager.rentals.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "users_id")
    private Users users;
}
