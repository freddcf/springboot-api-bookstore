package com.fredfonseca.bookstoremanager.author.builder;

import com.fredfonseca.bookstoremanager.author.dto.AuthorDTO;
import lombok.Builder;

@Builder
public class AuthorDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Fred Fonseca";

    @Builder.Default
    private final int age = 17;

    public AuthorDTO buildAuthorDTO() {
        return new AuthorDTO(id, name, age);
    }
}
