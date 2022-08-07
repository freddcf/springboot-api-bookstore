package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

public class InvalidDateException extends EntityExistsException {
    public InvalidDateException(LocalDate rentalDate, LocalDate returnForecast) {
        super(String.format("Data do aluguel %s não deve ser antes ou no dia da previsão de retorno %s", rentalDate, rentalDate));
    }
}
