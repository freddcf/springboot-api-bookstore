package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class RentalAlreadyExistsException extends EntityExistsException {
    public RentalAlreadyExistsException(Object book, Object user) {
        super(String.format("The user %s has already rented the book %s!", user, book));
    }
}
