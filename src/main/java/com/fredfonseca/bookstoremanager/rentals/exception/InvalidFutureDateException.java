package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidFutureDateException extends IllegalArgumentException {
    public InvalidFutureDateException() {
        super("You may not rent a book yesterday!");
    }
}
