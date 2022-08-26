package com.fredfonseca.bookstoremanager.users.exception;

public class InvalidCredentialsChange extends IllegalArgumentException {
    public InvalidCredentialsChange() {
        super("You may only change one of your unique credentials at time!");
    }

    public InvalidCredentialsChange(String userName) {
        super(String.format("You are not allowed to change the credentials of %s", userName));
    }
}