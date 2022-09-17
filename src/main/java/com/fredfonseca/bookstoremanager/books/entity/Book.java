package com.fredfonseca.bookstoremanager.books.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
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

    @Column(nullable = false, unique = true, length = 45)
    private String name;

    @Column(columnDefinition = "integer default 0")
    private int quantity;

    @Column(columnDefinition = "integer default 0")
    private int rentedQuantity;

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column(nullable = false, length = 45)
    private String author;

    @JsonManagedReference
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Rental> rentals;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
