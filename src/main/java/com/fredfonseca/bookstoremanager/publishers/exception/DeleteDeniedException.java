package com.fredfonseca.bookstoremanager.publishers.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Deleção negada! Esta editora possui um ou mais livros cadastrados");
    }
}
