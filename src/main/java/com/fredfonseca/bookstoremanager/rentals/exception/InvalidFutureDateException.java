package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class InvalidFutureDateException extends EntityExistsException {
    public InvalidFutureDateException() {
        super("You may not rent a book yesterday!");
    }
}
