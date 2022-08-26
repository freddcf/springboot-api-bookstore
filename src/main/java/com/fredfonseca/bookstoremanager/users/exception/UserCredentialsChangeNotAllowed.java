package com.fredfonseca.bookstoremanager.users.exception;

public class UserCredentialsChangeNotAllowed extends IllegalArgumentException {
    public UserCredentialsChangeNotAllowed() {
        super("Incorrect Password");
    }
}
