package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class RentalChangeNotAllowedException extends EntityExistsException {
    public RentalChangeNotAllowedException() {
        super(String.format("You are not allowed to change the status of this rent!"));
    }
}
