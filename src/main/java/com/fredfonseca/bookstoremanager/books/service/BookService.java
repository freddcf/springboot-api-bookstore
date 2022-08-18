package com.fredfonseca.bookstoremanager.books.service;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.fredfonseca.bookstoremanager.books.exception.BookNotFoundException;
import com.fredfonseca.bookstoremanager.books.exception.DeleteDeniedException;
import com.fredfonseca.bookstoremanager.books.exception.InvalidDateException;
import com.fredfonseca.bookstoremanager.books.mapper.BookMapper;
import com.fredfonseca.bookstoremanager.books.repository.BookRepository;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.publishers.service.PublisherService;
import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookService {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRepository bookRepository;

    private PublisherService publisherService;

    private RentalRepository rentalRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PublisherService publisherService, RentalRepository rentalRepository) {
        this.bookRepository = bookRepository;
        this.publisherService = publisherService;
        this.rentalRepository = rentalRepository;
    }

    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        verifyIfExists(bookRequestDTO.getName());
        Publisher foundPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherId());
        validateDate(bookRequestDTO);

        Book bookToSave = bookMapper.toModel(bookRequestDTO);
        bookToSave.setPublisher(foundPublisher);

        Book savedBook = bookRepository.save(bookToSave);
        return bookMapper.toDTO(savedBook);
    }

    public BookResponseDTO findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Page<BookResponseDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDTO);
    }

    public void delete(Long id) {
        Book bookToDelete = verifyAndGetIfExists(id);
        if(rentalRepository.findByBook(bookToDelete).isEmpty()) throw new DeleteDeniedException();
        bookRepository.deleteById(id);
    }

    public BookResponseDTO update(Long id, BookRequestDTO bookRequestDTO) {
        Book foundBook = verifyAndGetIfExists(id);
        Publisher foundPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherId());
        validateDate(bookRequestDTO);

        Book bookToUpdate = bookMapper.toModel(bookRequestDTO);
        bookToUpdate.setId(id);
        bookToUpdate.setPublisher(foundPublisher);
        bookToUpdate.setLaunchDate(foundBook.getLaunchDate());
        Book updatedBook = bookRepository.save(bookToUpdate);
        return bookMapper.toDTO(updatedBook);
    }

    public Book verifyAndGetIfExists(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private void verifyIfExists(String name) {
        Optional<Book> duplicatedBook = bookRepository
                .findByName(name);
        if(duplicatedBook.isPresent()) throw new BookAlreadyExistsException(name);
    }

    private void validateDate(BookRequestDTO bookRequestDTO) {
        LocalDate today = LocalDate.now();
        if(bookRequestDTO.getLaunchDate().isAfter(today))
            throw new InvalidDateException();
    }
}
