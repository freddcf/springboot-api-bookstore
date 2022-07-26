package com.fredfonseca.bookstoremanager.books.controller;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import com.fredfonseca.bookstoremanager.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "*")
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

    @GetMapping("/{id}")
    public BookResponseDTO findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable Long id, @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        return bookService.update(id, bookRequestDTO);
    }
}
