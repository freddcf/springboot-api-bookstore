package com.fredfonseca.bookstoremanager.books.dto;

import com.fredfonseca.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {

    private Long id;

    private String name;

    private Integer quantity;

    private LocalDate launchDate;

    private String author;

    private PublisherDTO publisher;
}
