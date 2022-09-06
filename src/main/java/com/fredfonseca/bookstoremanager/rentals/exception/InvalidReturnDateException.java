package com.fredfonseca.bookstoremanager.rentals.exception;

import java.time.LocalDate;

public class InvalidReturnDateException extends IllegalArgumentException {
    public InvalidReturnDateException(LocalDate rentalDate, LocalDate returnDate) {
        super(String.format("A data de retorno %s não deve ser antes da data de aluguel %s", returnDate, rentalDate));
    }
}