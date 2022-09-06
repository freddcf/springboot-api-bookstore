package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UserEmailAlreadyExistsException extends EntityExistsException {
    public UserEmailAlreadyExistsException(String email) {
        super(String.format("Usuário com email %s já existe", email));
    }
}