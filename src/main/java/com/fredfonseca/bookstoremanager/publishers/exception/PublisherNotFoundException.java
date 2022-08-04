package com.fredfonseca.bookstoremanager.publishers.exception;

import javax.persistence.EntityNotFoundException;

public class PublisherNotFoundException extends EntityNotFoundException {
    public PublisherNotFoundException(Long id) {
        super(String.format("Publisher with id %s not exists", id));
    }

    public PublisherNotFoundException(String name) {
        super(String.format("Publisher with name %s not exists", name));
    }
}
