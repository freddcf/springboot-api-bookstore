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
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        String rentStatus = "Não devolvido";

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);
        rentToSave.setReturnDate(rentStatus);

        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Rental savedRent = rentToSave;
        Book alterBook = rentToSave.getBook();

        validateDate(rentalRequestDTO, rentToSave, savedRent);

        alterBook.setQuantity(alterBook.getQuantity() - 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() + 1);

        savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    public RentalResponseDTO findById(Long id) {
        return rentalRepository.findById(id)
                .map(rentalMapper::toDTO)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    public List<RentalResponseDTO> findAll() {
        return rentalRepository.findAll().stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Rental rentalToDelete = verifyIfExists(id);
        Book alterBook = rentalToDelete.getBook();
        alterBook.setQuantity(alterBook.getQuantity() + 1);
        alterBook.setRentedQuantity(alterBook.getRentedQuantity() - 1);

        rentalRepository.deleteById(id);
    }

    public RentalResponseDTO update(Long id, RentalRequestUpdateDTO rentalRequestUpdateDTO) {
        Rental foundRental = verifyIfExists(id);

        Rental rentToSave = foundRental;
        rentToSave.setReturnDate(validateReturnDate(rentalRequestUpdateDTO, foundRental));

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
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
        if(!duplicatedRent.isEmpty()) throw new RentAlreadyExistsException(book.getName(), user.getName());
    }

    private Rental verifyIfExists(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    private void validateDate(RentalRequestDTO rentalRequestDTO, Rental rentToSave, Rental savedRent) {
        LocalDate today = LocalDate.now();
        if(rentalRequestDTO.getRentalDate().isAfter(today))
            throw new InvalidFutureDateException();

        if(!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast())))
            throw new InvalidDateException();

        if(savedRent.getBook().getQuantity() <= 0)
            throw new InvalidBookQuantity(savedRent.getBook().getName());
    }
}
