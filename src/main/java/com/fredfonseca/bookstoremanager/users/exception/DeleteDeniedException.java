package com.fredfonseca.bookstoremanager.users.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Delete command denied! This user object is linked to a Rental!");
    }

    public DeleteDeniedException(String name, String role) {
        super(String.format("Sorry, %s a logged %s cannot delete itself...", name, role));
    }
}