package com.fredfonseca.bookstoremanager.rentals.service;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.rentals.exception.InvalidDateException;
import com.fredfonseca.bookstoremanager.rentals.exception.RentAlreadyExistsException;
import com.fredfonseca.bookstoremanager.rentals.mapper.RentalMapper;
import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalMapper rentalMapper = RentalMapper.INSTANCE;

    private RentalRepository rentalRepository;

    private BookService bookService;

    private UserService userService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, BookService bookService, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public RentalResponseDTO create(RentalRequestDTO rentalRequestDTO) throws InvalidDateException {
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);

        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Rental savedRent = null;

        if(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast())){
            savedRent = rentalRepository.save(rentToSave);
        } else {
            throw new InvalidDateException(rentToSave.getRentalDate(), rentToSave.getReturnForecast());
        }
        return rentalMapper.toDTO(savedRent);
    }

    private void verifyIfExists(Book book, Users user) {

        List<Rental> duplicatedRent = rentalRepository
                .findByBookAndUsers(book, user);
        if(!duplicatedRent.isEmpty()) throw new RentAlreadyExistsException(book.getName(), user.getName());
    }

    private void validateRentDate() {

    }
}
