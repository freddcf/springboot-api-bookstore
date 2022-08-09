package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class InvalidDateException extends EntityExistsException {
    public InvalidDateException() {
        super("Rental date should not be before or the same day of the return forecast ");
    }
}

