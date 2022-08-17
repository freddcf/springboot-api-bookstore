package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class InvalidDateException extends EntityExistsException {
    public InvalidDateException() {
        super("Launch date should not be in a future date");
    }
}

