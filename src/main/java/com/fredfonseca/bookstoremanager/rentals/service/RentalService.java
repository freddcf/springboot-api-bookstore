package com.fredfonseca.bookstoremanager.rentals.service;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestUpdateDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.rentals.enums.RentalReturnStates;
import com.fredfonseca.bookstoremanager.rentals.exception.*;
import com.fredfonseca.bookstoremanager.rentals.mapper.RentalMapper;
import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.dto.AuthenticatedUser;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.enums.Role;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    private final RentalMapper rentalMapper = RentalMapper.INSTANCE;

    private RentalRepository rentalRepository;

    private BookService bookService;

    private UserService userService;

    private final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private final String RENTAL_DEFAULT = RentalReturnStates.INITIAL_VALUE.getDescription();
    private final String RENTAL_RETURNED_EARLY = RentalReturnStates.EARLY.getDescription();
    private final String RENTAL_RETURNED_LATE = RentalReturnStates.LATE.getDescription();

    @Autowired
    public RentalService(RentalRepository rentalRepository, BookService bookService, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public RentalResponseDTO create(AuthenticatedUser authenticatedUser, RentalRequestDTO rentalRequestDTO) {
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        if(!isAdmin(foundAuthenticatedUser)) {
            foundUser = foundAuthenticatedUser;
        }
        String rentStatus = RENTAL_DEFAULT;

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);
        rentToSave.setReturnDate(rentStatus);
        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Book alterBook = rentToSave.getBook();

        validateData(rentalRequestDTO, rentToSave, alterBook);
        alterBook.setQuantity(alterBook.getQuantity() - 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() + 1);

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    public RentalResponseDTO findById(Long id, AuthenticatedUser authenticatedUser) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        if (isAdmin(foundAuthenticatedUser)) {
            return rentalRepository.findById(id)
                    .map(rentalMapper::toDTO)
                    .orElseThrow(() -> new RentalNotFoundException(id));
        }
        return rentalRepository.findByIdAndUsers(id, foundAuthenticatedUser)
                .map(rentalMapper::toDTO)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    public Page<RentalResponseDTO> findAll(AuthenticatedUser authenticatedUser, Pageable pageable) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        if (isAdmin(foundAuthenticatedUser)) {
            return rentalRepository.findAll(pageable).map(rentalMapper::toDTO);
        }
        return rentalRepository.findAllByUsers(foundAuthenticatedUser, pageable)
                .map(rentalMapper::toDTO);
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {
        userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Rental rentalToDelete = verifyIfExists(id);

        if(rentalToDelete.getReturnDate().equals(RENTAL_DEFAULT)) {
            returnBookToStock(rentalToDelete.getBook());
        }

        rentalRepository.deleteById(id);
    }

    public RentalResponseDTO update(Long id, AuthenticatedUser authenticatedUser, RentalRequestUpdateDTO rentalRequestUpdateDTO) {
        userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Rental foundRental = verifyIfExists(id);

        if(!foundRental.getReturnDate().equals(RENTAL_DEFAULT)){
            throw new BookAlreadyReturnedException(foundRental.getUsers().getName(), foundRental.getBook().getName());
        }

        Rental rentToSave = foundRental;
        rentToSave.setReturnDate(validateReturnDate(rentalRequestUpdateDTO, foundRental));
        returnBookToStock(rentToSave.getBook());

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    private boolean isAdmin(Users user) {
        return user.getRole().toString().equals(ROLE_ADMIN);
    }

    private String validateReturnDate(RentalRequestUpdateDTO rentalRequestUpdateDTO, Rental foundRental) {
        String rentStatus = "";
        LocalDate returnDate = rentalRequestUpdateDTO.getReturnDate();
        LocalDate rentalDate = foundRental.getRentalDate();
        LocalDate returnForecast = foundRental.getReturnForecast();

        if (returnDate.isBefore(rentalDate)) {
            throw new InvalidReturnDateException(rentalDate, returnDate);
        }
        if (returnDate.isAfter(returnForecast)) {
            rentStatus = returnDate + " " + RENTAL_RETURNED_LATE;
        }
        if (returnDate.isBefore(returnForecast) || returnDate.isEqual(returnForecast)) {
            rentStatus = returnDate + " " + RENTAL_RETURNED_EARLY;
        }
        return rentStatus;
    }

    private void returnBookToStock(Book book) {
        Book alterBook = book;
        alterBook.setQuantity(alterBook.getQuantity() + 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() - 1);
    }

    private void verifyIfExists(Book book, Users user) {

        List<Rental> duplicatedRent = rentalRepository
                .findByBookAndUsers(book, user);
        if (!duplicatedRent.isEmpty()) {
            throw new RentalAlreadyExistsException(book.getName(), user.getName());
        }
    }

    private Rental verifyIfExists(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    private void validateData(RentalRequestDTO rentalRequestDTO, Rental rentToSave, Book book) {
        LocalDate today = LocalDate.now();
        if (rentalRequestDTO.getRentalDate().isBefore(today)) {
            throw new InvalidPatDateException();
        }

        if (!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast()))) {
            throw new InvalidDateException();
        }

        if (book.getQuantity() <= 0) {
            throw new InvalidBookQuantity(book.getName());
        }
    }
}