package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class InvalidCredentialsChange extends EntityExistsException {
    public InvalidCredentialsChange() {
        super("You may only change one of your unique credentials at time!");
    }
}