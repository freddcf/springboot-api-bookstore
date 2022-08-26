package com.fredfonseca.bookstoremanager.books.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Delete command denied! This book object is linked to a Rental!");
    }
}
