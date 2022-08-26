package com.fredfonseca.bookstoremanager.books.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException() {
        super("Launch date should not be in a future date");
    }
}

