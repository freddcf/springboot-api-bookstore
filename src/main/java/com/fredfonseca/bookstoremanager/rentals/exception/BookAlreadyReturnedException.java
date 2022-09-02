package com.fredfonseca.bookstoremanager.rentals.exception;

public class BookAlreadyReturnedException extends IllegalArgumentException {
    public BookAlreadyReturnedException(String user, String book) {
        super(String.format("The user %s has already returned the book %s!", user, book));
    }
}
