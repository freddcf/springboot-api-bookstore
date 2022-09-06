package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UsernameAlreadyExistsException extends EntityExistsException {
    public UsernameAlreadyExistsException(String username) {
        super(String.format("Usuário com username %s já existe", username));
    }
}
