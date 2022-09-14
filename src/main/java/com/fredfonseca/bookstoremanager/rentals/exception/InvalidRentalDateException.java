package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidRentalDateException extends IllegalArgumentException {
    public InvalidRentalDateException() {
        super("Você não pode alugar um livro no futuro");
    }
    public InvalidRentalDateException(int min) {
        super(String.format("Você não pode registrar um aluguel antes de %s dias atrás", min));
    }
}
