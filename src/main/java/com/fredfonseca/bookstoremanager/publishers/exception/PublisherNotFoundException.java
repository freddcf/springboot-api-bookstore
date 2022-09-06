package com.fredfonseca.bookstoremanager.publishers.exception;

import javax.persistence.EntityNotFoundException;

public class PublisherNotFoundException extends EntityNotFoundException {
    public PublisherNotFoundException(Long id) {
        super(String.format("Editora com id %s não existe", id));
    }

    public PublisherNotFoundException(String name) {
        super(String.format("Editora com o nome %s não existe", name));
    }
}
