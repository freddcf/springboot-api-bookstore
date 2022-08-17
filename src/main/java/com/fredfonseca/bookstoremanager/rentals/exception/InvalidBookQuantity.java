package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityNotFoundException;

public class InvalidBookQuantity extends EntityNotFoundException {
    public InvalidBookQuantity(String book) {
        super(String.format("There is no more left of %s!", book));
    }
}
