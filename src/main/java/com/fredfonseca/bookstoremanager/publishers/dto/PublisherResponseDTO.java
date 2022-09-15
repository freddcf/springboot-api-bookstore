package com.fredfonseca.bookstoremanager.publishers.dto;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherResponseDTO {

    private Long id;

    private String name;

    private String city;

    private List<Book> books;
}
