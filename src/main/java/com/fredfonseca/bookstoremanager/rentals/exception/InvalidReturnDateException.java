package com.fredfonseca.bookstoremanager.rentals.exception;

import java.time.LocalDate;

public class InvalidReturnDateException extends IllegalArgumentException {
    public InvalidReturnDateException(LocalDate rentalDate, LocalDate returnDate) {
        super(String.format("Return date %s should not be before rental date %s!", returnDate, rentalDate));
    }
}