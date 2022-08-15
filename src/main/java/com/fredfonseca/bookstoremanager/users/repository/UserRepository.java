package com.fredfonseca.bookstoremanager.users.repository;

import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findAllByUsername(String user);
}
