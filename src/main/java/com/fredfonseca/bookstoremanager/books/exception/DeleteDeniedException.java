package com.fredfonseca.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;

public class DeleteDeniedException extends EntityExistsException {
    public DeleteDeniedException() {
        super("Delete command denied! This book object is linked to a Rental!");
    }
}
