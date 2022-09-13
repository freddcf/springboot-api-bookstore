package com.fredfonseca.bookstoremanager.rentals.exception;

import javax.persistence.EntityExistsException;

public class RentalAlreadyExistsException extends EntityExistsException {
    public RentalAlreadyExistsException(Object book, Object user) {
        super(String.format("O usuário %s está atualmente com o livro %s alugado", user, book));
    }
}
