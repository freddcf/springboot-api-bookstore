package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class BookAlreadyExistsException extends EntityExistsException {
    public BookAlreadyExistsException(String name) {
        super(String.format("Livro com nome %s já existe", name));
    }
}
