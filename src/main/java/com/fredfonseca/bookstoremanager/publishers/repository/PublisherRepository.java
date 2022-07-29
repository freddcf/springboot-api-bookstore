package com.fredfonseca.bookstoremanager.publishers.repository;

import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
