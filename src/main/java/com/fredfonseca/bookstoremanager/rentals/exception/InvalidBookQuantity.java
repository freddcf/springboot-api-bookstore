package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class InvalidBookQuantity extends EntityExistsException {
    public InvalidBookQuantity(String book) {
        super(String.format("There is no more left of %s!", book));
    }
}
