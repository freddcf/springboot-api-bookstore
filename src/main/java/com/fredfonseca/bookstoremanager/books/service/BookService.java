package com.fredfonseca.bookstoremanager.books.service;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import com.fredfonseca.bookstoremanager.books.entity.Book;
import com.fredfonseca.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.fredfonseca.bookstoremanager.books.exception.BookNotFoundException;
import com.fredfonseca.bookstoremanager.books.mapper.BookMapper;
import com.fredfonseca.bookstoremanager.books.repository.BookRepository;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.publishers.service.PublisherService;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRepository bookRepository;

    private UserService userService;

    private PublisherService publisherService;

    @Autowired
    public BookService(BookRepository bookRepository, UserService userService, PublisherService publisherService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.publisherService = publisherService;
    }

    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        verifyIfExists(bookRequestDTO.getName());
        Publisher foundPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherName());

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

    private void verifyIfExists(String name) {
        Optional<Book> duplicatedBook = bookRepository
                .findByName(name);
        if(duplicatedBook.isPresent()) throw new BookAlreadyExistsException(name);
    }
}
