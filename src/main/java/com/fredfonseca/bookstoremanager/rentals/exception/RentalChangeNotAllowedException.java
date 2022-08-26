package com.fredfonseca.bookstoremanager.rentals.exception;

public class RentalChangeNotAllowedException extends IllegalArgumentException {
    public RentalChangeNotAllowedException() {
        super(String.format("You are not allowed to change the status of this rent!"));
    }
}
