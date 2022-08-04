package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class BookAlreadyExistsException extends EntityExistsException {
    public BookAlreadyExistsException(String name, String author) {
        super(String.format("Book with name %s and author %s already exists", name, author));
    }
}
