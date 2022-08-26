package com.fredfonseca.bookstoremanager.publishers.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Delete command denied! This publisher object is linked to a book!");
    }
}
