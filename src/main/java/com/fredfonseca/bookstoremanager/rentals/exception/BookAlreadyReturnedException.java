package com.fredfonseca.bookstoremanager.rentals.exception;

public class BookAlreadyReturnedException extends IllegalArgumentException {
    public BookAlreadyReturnedException(String user, String book) {
        super(String.format("O usuário %s já devolveu o livro %s", user, book));
    }
}
