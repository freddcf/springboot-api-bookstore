package com.fredfonseca.bookstoremanager.rentals.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException() {
        super("A previsão de retorno não deve ser antes da data de aluguel");
    }
}
