package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidPastDateException extends IllegalArgumentException {
    public InvalidPastDateException() {
        super("Você não pode alugar um livro no futuro");
    }
}
