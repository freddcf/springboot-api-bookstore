package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityNotFoundException;

public class EmptyReturnDateException extends EntityNotFoundException {
    public EmptyReturnDateException() {
        super("Return date should not be null on returning a book!");
    }
}
