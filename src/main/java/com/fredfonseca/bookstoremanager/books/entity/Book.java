package com.fredfonseca.bookstoremanager.books.entity;

import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column(nullable = false)
    private String author;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Publisher publisher;
}
