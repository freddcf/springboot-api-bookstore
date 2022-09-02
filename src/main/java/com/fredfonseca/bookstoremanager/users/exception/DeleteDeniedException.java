package com.fredfonseca.bookstoremanager.users.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Delete command denied! This user object is linked to a Rental!");
    }
}