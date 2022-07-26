package com.fredfonseca.bookstoremanager.users.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.users.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(unique = true, length = 30)
    private String username;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Rental> rentals;
}

