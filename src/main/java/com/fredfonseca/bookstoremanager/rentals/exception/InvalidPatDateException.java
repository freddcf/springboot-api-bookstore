package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidPatDateException extends IllegalArgumentException {
    public InvalidPatDateException() {
        super("Você não pode alugar um livro para uma data passada");
    }
}
