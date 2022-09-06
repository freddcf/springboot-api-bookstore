package com.fredfonseca.bookstoremanager.users.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long id) {
        super(String.format("Usuário com ID %s não existe", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("Usuário com username %s não existe", username));
    }
}
