package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidBookQuantity extends IllegalArgumentException {
    public InvalidBookQuantity(String book) {
        super(String.format("There is no more left of %s!", book));
    }
}
