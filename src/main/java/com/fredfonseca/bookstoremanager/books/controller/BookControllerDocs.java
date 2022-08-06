package com.fredfonseca.bookstoremanager.books.controller;

import com.fredfonseca.bookstoremanager.books.dto.BookRequestDTO;
import com.fredfonseca.bookstoremanager.books.dto.BookResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api("Books module management")
public interface BookControllerDocs {

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or author already registered on system")
    })
    BookResponseDTO create(BookRequestDTO bookRequestDTO);

    @ApiOperation(value = "Find book by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success book found"),
            @ApiResponse(code = 404, message = "Book not found error")
    })
    BookResponseDTO findById(Long id);

    @ApiOperation(value = "List all books operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success book list found"),
    })
    List<BookResponseDTO> findAll();

    @ApiOperation(value = "Book delete operation")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success book exclusion"),
            @ApiResponse(code = 404, message = "Book not found error")
    })
    void delete(Long id);

    @ApiOperation(value = "Book update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success book updated"),
            @ApiResponse(code = 400, message = "Missing required field, or an error on validation field rules")
    })
    BookResponseDTO update(Long id, BookRequestDTO bookRequestDTO);
}
