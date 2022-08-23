package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class InvalidQuantityException extends EntityExistsException {
    public InvalidQuantityException() {
        super("Invalid book quantity!");
    }
}
