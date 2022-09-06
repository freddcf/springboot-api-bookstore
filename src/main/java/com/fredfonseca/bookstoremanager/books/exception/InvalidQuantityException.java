package com.fredfonseca.bookstoremanager.books.exception;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super("Quantidade de livros inválida");
    }
}
