package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityNotFoundException;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(Long id) {
        super(String.format("Livro com ID %s não existe", id));
    }
}
