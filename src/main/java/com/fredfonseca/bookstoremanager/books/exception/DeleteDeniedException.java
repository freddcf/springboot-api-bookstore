package com.fredfonseca.bookstoremanager.books.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Deleção negada! Este livro foi utilizado em um ou mais aluguéis");
    }
}
