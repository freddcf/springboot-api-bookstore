package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UserCredentialsChangeNotAllowed extends EntityExistsException {
    public UserCredentialsChangeNotAllowed() {
        super("Incorrect Password");
    }
}
