package com.fredfonseca.bookstoremanager.rentals.dto;

import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
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

    private BookResponseDTO book;

    private UserDTO users;

    private LocalDate rentalDate;

    private LocalDate returnForecast;

    private String returnDate;
}
