package com.fredfonseca.bookstoremanager.users.exception;

public class InvalidCredentialsChange extends IllegalArgumentException {
    public InvalidCredentialsChange() {
        super("Você só pode mudar uma de suas credenciais únicas por vez");
    }

    public InvalidCredentialsChange(String type) {
        super(String.format("O ID informado não pertence a um %s", type));
    }
}