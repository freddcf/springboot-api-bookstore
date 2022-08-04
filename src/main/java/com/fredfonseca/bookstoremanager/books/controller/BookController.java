package com.fredfonseca.bookstoremanager.books.controller;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController implements  BookControllerDocs{

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(@RequestBody @Valid BookRequestDTO bookRequestDTO) {
        return bookService.create(bookRequestDTO);
    }
}
