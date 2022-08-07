package com.fredfonseca.bookstoremanager.rentals.service;

import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import com.fredfonseca.bookstoremanager.rentals.exception.InvalidBookQuantity;
import com.fredfonseca.bookstoremanager.rentals.exception.InvalidDateException;
import com.fredfonseca.bookstoremanager.rentals.exception.RentAlreadyExistsException;
import com.fredfonseca.bookstoremanager.rentals.exception.RentalNotFoundException;
import com.fredfonseca.bookstoremanager.rentals.mapper.RentalMapper;
import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);

        verifyIfExists(rentToSave.getBook(), rentToSave.getUsers());

        Rental savedRent = rentToSave;
        Book alterBook = rentToSave.getBook();

        if(!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast())))
            throw new InvalidDateException(rentToSave.getRentalDate(), rentToSave.getReturnForecast());

        if(savedRent.getBook().getQuantity() <= 0)
            throw new InvalidBookQuantity(savedRent.getBook().getName());

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

    public RentalResponseDTO update(Long id, RentalRequestDTO rentalRequestDTO) {
        Rental foundRental = verifyIfExists(id);

        Users foundUser = userService.verifyAndGetIfExists(rentalRequestDTO.getUserId());
        Book foundBook = bookService.verifyAndGetIfExists(rentalRequestDTO.getBookId());

        Rental rentToSave = rentalMapper.toModel(rentalRequestDTO);
        rentToSave.setId(id);
        rentToSave.setBook(foundBook);
        rentToSave.setUsers(foundUser);

        Rental savedRent = null;

        if(!(rentToSave.getRentalDate().isBefore(rentToSave.getReturnForecast())))
            throw new InvalidDateException(rentToSave.getRentalDate(), rentToSave.getReturnForecast());

        savedRent = rentalRepository.save(rentToSave);
        return rentalMapper.toDTO(savedRent);
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

    private void validateRentDate() {

    }
}
