package com.fredfonseca.bookstoremanager.books.dto;

import com.fredfonseca.bookstoremanager.publishers.dto.PublisherRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {

    private Long id;

    private String name;

    private Integer quantity;

    private Integer rentedQuantity;

    private LocalDate launchDate;

    private String author;

    private PublisherRequestDTO publisher;

    private List<Rental> rentals;
}
