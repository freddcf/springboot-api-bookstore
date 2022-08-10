package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class DeleteDeniedException extends EntityExistsException {
    public DeleteDeniedException() {
        super("Delete command denied! This user object is linked to a Rental!");
    }
}