package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

public class InvalidDateException extends EntityExistsException {
    public InvalidDateException(LocalDate rentalDate, LocalDate returnForecast) {
        super(String.format("Rental date %s should not be before or the same day of the return forecast %s", rentalDate, returnForecast));
    }
}
