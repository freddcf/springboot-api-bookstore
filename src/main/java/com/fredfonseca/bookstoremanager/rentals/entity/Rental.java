package com.fredfonseca.bookstoremanager.rentals.entity;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
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

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Book book;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Users users;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate rentalDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate returnForecast;

    @Column
    private String returnDate = "Não devolvido";
}
