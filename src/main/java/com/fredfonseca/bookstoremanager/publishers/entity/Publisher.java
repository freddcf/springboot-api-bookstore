package com.fredfonseca.bookstoremanager.publishers.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;
}
