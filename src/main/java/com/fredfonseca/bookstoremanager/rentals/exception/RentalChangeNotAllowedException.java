package com.fredfonseca.bookstoremanager.rentals.exception;

public class RentalChangeNotAllowedException extends IllegalArgumentException {
    public RentalChangeNotAllowedException() {
        super(String.format("Você não tem permissão para mudar os status desse aluguel"));
    }
}
