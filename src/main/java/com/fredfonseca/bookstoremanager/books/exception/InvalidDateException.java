package com.fredfonseca.bookstoremanager.books.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException() {
        super("Data de lançamento não deve ser em uma data futura");
    }
}

