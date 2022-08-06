package com.fredfonseca.bookstoremanager.books.entity;

import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "integer default 0")
    private int quantity;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate launchDate;

    @Column(nullable = false)
    private String author;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Rental> rentals;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Publisher publisher;
}
