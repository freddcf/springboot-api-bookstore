package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidBookQuantity extends IllegalArgumentException {
    public InvalidBookQuantity(String book) {
        super(String.format("Não há mais exemplares de %s", book));
    }
}
