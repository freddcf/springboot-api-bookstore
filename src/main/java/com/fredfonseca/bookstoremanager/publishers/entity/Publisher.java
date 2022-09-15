package com.fredfonseca.bookstoremanager.publishers.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String name;

    @Column(nullable = false, length = 30)
    private String city;

    @JsonManagedReference
    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    private List<Book> books;
}
