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
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.enums.Role;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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

    public RentalResponseDTO create(RentalRequestDTO rentalRequestDTO) {
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        String rentStatus = RENTAL_DEFAULT;

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);
        rentToSave.setReturnDate(rentStatus);
        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Book alterBook = rentToSave.getBook();
        validateData(rentalRequestDTO, rentToSave, alterBook);
        manageBookStock(alterBook, false);

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    public RentalResponseDTO findById(Long id) {
            return rentalRepository.findById(id)
                    .map(rentalMapper::toDTO)
                    .orElseThrow(() -> new RentalNotFoundException(id));
    }

    public Page<RentalResponseDTO> findAll(Pageable pageable) {
            return rentalRepository.findAll(pageable).map(rentalMapper::toDTO);
    }

    public void delete(Long id) {
        Rental rentalToDelete = verifyIfExists(id);

        if(rentalToDelete.getReturnDate().equals(RENTAL_DEFAULT)) {
            manageBookStock(rentalToDelete.getBook(), true);
        }
        rentalRepository.deleteById(id);
    }

    public RentalResponseDTO update(Long id, RentalRequestDTO rentalRequestDTO) {
        Rental foundRental = verifyIfExists(id);
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());
        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        checkBookAlreadyReturned(foundRental);

        Rental rentToUpdate = rentalMapper.toModel(rentalRequestDTO);
        rentToUpdate.setId(foundRental.getId());
        rentToUpdate.setUsers(foundUser);
        rentToUpdate.setBook(foundBook);
        rentToUpdate.setReturnDate(foundRental.getReturnDate());
        if(!foundRental.getBook().equals(rentToUpdate.getBook())){
            manageBookStock(foundRental.getBook(), true);
            manageBookStock(foundBook, false);
        }
        validateData(rentalRequestDTO, rentToUpdate, foundBook);

        Rental updatedRent = rentalRepository.save(rentToUpdate);
        return rentalMapper.toDTO(updatedRent);
    }

    public RentalResponseDTO returnBook(Long id) {
        Rental foundRental = verifyIfExists(id);

        checkBookAlreadyReturned(foundRental);

        Rental rentToSave = foundRental;
        rentToSave.setReturnDate(validateReturnDate(foundRental));
        manageBookStock(rentToSave.getBook(), true);

        Rental savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
    }

    private void checkBookAlreadyReturned(Rental foundRental) {
        if(!foundRental.getReturnDate().equals(RENTAL_DEFAULT)){
            throw new BookAlreadyReturnedException(foundRental.getUsers().getName(), foundRental.getBook().getName());
        }
    }

    private String validateReturnDate(Rental foundRental) {
        String rentStatus = "";
        LocalDate returnDate = LocalDate.now();
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

    private void manageBookStock(Book book, boolean addToStock) {
        if(addToStock) {
            Book alterBook = book;
            alterBook.setQuantity(alterBook.getQuantity() + 1);
            alterBook.setRentedQuantity(alterBook.getRentedQuantity() - 1);
        } else {
            book.setQuantity(book.getQuantity() - 1);
            book.setRentedQuantity(book.getRentedQuantity() + 1);
        }
    }

    private void verifyIfExists(Book book, Users user) {
        List<Rental> duplicatedRent = rentalRepository
                .findByBookAndUsers(book, user);
        if (!duplicatedRent.isEmpty()) {
            duplicatedRent.forEach(rental -> {
                if(rental.getReturnDate().equals(RENTAL_DEFAULT)) {
                    throw new RentalAlreadyExistsException(book.getName(), user.getName());
                }
            });
        }
    }

    private Rental verifyIfExists(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    private void validateData(RentalRequestDTO rentalRequestDTO, Rental rentToSave, Book book) {
        LocalDate today = LocalDate.now();
        if (rentalRequestDTO.getRentalDate().isAfter(today)) {
            throw new InvalidPastDateException();
        }

        if (!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast()))) {
            throw new InvalidDateException();
        }

        if (book.getQuantity() <= 0) {
            throw new InvalidBookQuantity(book.getName());
        }
    }
}