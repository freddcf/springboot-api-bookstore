package com.fredfonseca.bookstoremanager.rentals.service;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestUpdateDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.rentals.exception.*;
import com.fredfonseca.bookstoremanager.rentals.mapper.RentalMapper;
import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.dto.AuthenticatedUser;
import com.fredfonseca.bookstoremanager.users.entity.Users;
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

    @Autowired
    public RentalService(RentalRepository rentalRepository, BookService bookService, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public RentalResponseDTO create(RentalRequestDTO rentalRequestDTO) {
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        String rentStatus = "Não devolvido";

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);
        rentToSave.setReturnDate(rentStatus);
        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Book alterBook = rentToSave.getBook();

        validateDate(rentalRequestDTO, rentToSave, alterBook);
        alterBook.setQuantity(alterBook.getQuantity() - 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() + 1);

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    public RentalResponseDTO findById(Long id, AuthenticatedUser authenticatedUser) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        if(isAdmin(foundAuthenticatedUser)) {
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
        if(isAdmin(foundAuthenticatedUser)) {
            return rentalRepository.findAll(pageable)
                    .map(rentalMapper::toDTO);
        }
        return rentalRepository.findAllByUsers(foundAuthenticatedUser, pageable)
                .map(rentalMapper::toDTO);
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Rental rentalToDelete = verifyIfExists(id);

        checkChangeStatusPermission(rentalToDelete.getUsers().getUsername(), foundAuthenticatedUser);

        Book alterBook = rentalToDelete.getBook();
        alterBook.setQuantity(alterBook.getQuantity() + 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() - 1);

        rentalRepository.deleteById(id);
    }

    public RentalResponseDTO update(Long id, AuthenticatedUser authenticatedUser, RentalRequestUpdateDTO rentalRequestUpdateDTO) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Rental foundRental = verifyIfExists(id);

        checkChangeStatusPermission(foundRental.getUsers().getUsername(), foundAuthenticatedUser);

        Rental rentToSave = foundRental;
        rentToSave.setReturnDate(validateReturnDate(rentalRequestUpdateDTO, foundRental));

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    private boolean isAdmin(Users foundAuthenticatedUser) {
        return foundAuthenticatedUser.getRole().toString().equals("ADMIN");
    }

    private void checkChangeStatusPermission(String foundUsername, Users authenticatedUser) {
        if(isAdmin(authenticatedUser)) return;
        if(!foundUsername.equals(authenticatedUser.getUsername()))
            throw new RentalChangeNotAllowedException();
    }

    private String validateReturnDate(RentalRequestUpdateDTO rentalRequestUpdateDTO, Rental foundRental) {
        String rentStatus = "";
        LocalDate returnDate = rentalRequestUpdateDTO.getReturnDate();
        LocalDate rentalDate = foundRental.getRentalDate();
        LocalDate returnForecast = foundRental.getReturnForecast();

        if(returnDate.isBefore(rentalDate))
            throw new InvalidReturnDateException(rentalDate, returnDate);

        if(returnDate.isAfter(returnForecast)) rentStatus = returnDate + " (Com atraso)";
        if(returnDate.isBefore(returnForecast) || returnDate.isEqual(returnForecast))
            rentStatus = returnDate + " (No prazo)";
        return rentStatus;
    }

    private void verifyIfExists(Book book, Users user) {

        List<Rental> duplicatedRent = rentalRepository
                .findByBookAndUsers(book, user);
        if(!duplicatedRent.isEmpty()) throw new RentalAlreadyExistsException(book.getName(), user.getName());
    }

    private Rental verifyIfExists(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    private void validateDate(RentalRequestDTO rentalRequestDTO, Rental rentToSave, Book book) {
        LocalDate today = LocalDate.now();
        if(rentalRequestDTO.getRentalDate().isAfter(today))
            throw new InvalidFutureDateException();

        if(!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast())))
            throw new InvalidDateException();

        if(book.getQuantity() <= 0)
            throw new InvalidBookQuantity(book.getName());
    }
}

/*
*--------------------------------------------------------
* IF WANT TO ALLOW RENT BOOKS PASSING THE USER AS THE
* AUTHENTICATED USER AND FORBID ADMIN TO RENT BOOKS
*--------------------------------------------------------
*
public RentalResponseDTO create(AuthenticatedUser authenticatedUser, RentalRequestDTO rentalRequestDTO) {
        Users foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        if(foundAuthenticatedUser.getRole().toString().equals("ADMIN")) throw new RentalCreationNotAllowed();

        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        String rentStatus = "Não devolvido";

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundAuthenticatedUser);
        rentToSave.setReturnDate(rentStatus);
        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Book alterBook = rentToSave.getBook();

        validateDate(rentalRequestDTO, rentToSave, alterbook);
        alterBook.setQuantity(alterBook.getQuantity() - 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() + 1);

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }
 */