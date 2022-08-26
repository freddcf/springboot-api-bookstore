package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException() {
        super("Return forecast may not be before the rental date");
    }
}
