package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityNotFoundException;

public class RentalNotFoundException extends EntityNotFoundException {
    public RentalNotFoundException(Long id) {
        super(String.format("Aluguel com ID %s não existe", id));
    }
}
