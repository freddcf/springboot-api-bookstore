package com.fredfonseca.bookstoremanager.users.exception;

public class InvalidCredentialsChange extends IllegalArgumentException {
    public InvalidCredentialsChange() {
        super("You may only change one of your unique credentials at time!");
    }

    public InvalidCredentialsChange(String type) {
        super(String.format("Error! The informed ID is not from a %s!", type));
    }
}