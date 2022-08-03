package com.fredfonseca.bookstoremanager.books.service;

import com.fredfonseca.bookstoremanager.books.mapper.BookMapper;
import com.fredfonseca.bookstoremanager.books.repository.BookRepository;
import com.fredfonseca.bookstoremanager.publishers.service.PublisherService;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
