package com.fredfonseca.bookstoremanager.users.repository;

import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
