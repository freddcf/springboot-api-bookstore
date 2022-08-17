package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class InvalidCredentialsChange extends EntityExistsException {
    public InvalidCredentialsChange() {
        super("You may only change one of your unique credentials at time!");
    }

    public InvalidCredentialsChange(String userName) {
        super(String.format("You are not allowed to change the credentials of %s", userName));
    }
}