package com.fredfonseca.bookstoremanager.books.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class BookDTO {

    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotNull
    @NotEmpty
    private int quantity;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate launchDate;
}
