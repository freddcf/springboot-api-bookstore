package com.fredfonseca.bookstoremanager.publishers.exception;

import javax.persistence.EntityExistsException;

public class PublisherAlreadyExistsException extends EntityExistsException {
    public PublisherAlreadyExistsException(String name) {
        super(String.format("Editora com o nome %s já existe", name));
    }
}
