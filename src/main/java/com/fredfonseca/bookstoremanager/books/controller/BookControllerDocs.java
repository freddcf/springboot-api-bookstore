package com.fredfonseca.bookstoremanager.books.controller;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Books module management")
public interface BookControllerDocs {

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or author already registered on system")
    })
    BookResponseDTO create(BookRequestDTO bookRequestDTO);

}
