package com.fredfonseca.bookstoremanager.rentals.dto;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponseDTO {

    private Long id;

    private Book book;

    private Users users;

    private LocalDate rentalDate;

    private LocalDate returnForecast;

    private String returnDate;
}
