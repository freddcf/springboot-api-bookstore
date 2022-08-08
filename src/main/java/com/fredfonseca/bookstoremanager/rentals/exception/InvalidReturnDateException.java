package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class InvalidReturnDateException extends EntityExistsException {
    public InvalidReturnDateException(LocalDate rentalDate, LocalDate returnDate) {
        super(String.format("Return date %s should not be before rental date %s!", returnDate, rentalDate));
    }
}
