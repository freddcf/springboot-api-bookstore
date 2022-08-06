package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UserAlreadyExistsException extends EntityExistsException {
    public UserAlreadyExistsException(String email) {
        super(String.format("User with email %s already exists!", email));
    }
}
