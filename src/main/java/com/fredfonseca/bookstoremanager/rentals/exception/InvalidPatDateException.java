package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidPatDateException extends IllegalArgumentException {
    public InvalidPatDateException() {
        super("You may not rent a book in a past date!");
    }
}
