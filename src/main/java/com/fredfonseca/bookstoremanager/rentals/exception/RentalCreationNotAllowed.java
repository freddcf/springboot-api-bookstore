package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class RentalCreationNotAllowed extends EntityExistsException {
    public RentalCreationNotAllowed() {
        super("An Administrator account MAY NOT rent books!");
    }
}
