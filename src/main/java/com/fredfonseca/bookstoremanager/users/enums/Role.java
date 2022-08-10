package com.fredfonseca.bookstoremanager.users.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {

    ADMIN("Admin"),
    USER("User");

    private final String description;
}
