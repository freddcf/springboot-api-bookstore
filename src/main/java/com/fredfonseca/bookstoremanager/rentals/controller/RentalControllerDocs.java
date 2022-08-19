package com.fredfonseca.bookstoremanager.rentals.controller;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestUpdateDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.users.dto.AuthenticatedUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalControllerDocs {

    @ApiOperation(value = "Rental creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book rent creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or rent already registered on system")
    })
    RentalResponseDTO create(AuthenticatedUser authenticatedUser, RentalRequestDTO rentalRequestDTO);

    @ApiOperation(value = "Find rent by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success rent found"),
            @ApiResponse(code = 404, message = "Rental not found error")
    })
    RentalResponseDTO findById(Long id, AuthenticatedUser authenticatedUser);

    @ApiOperation(value = "List all rents operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success rental list found")
    })
    Page<RentalResponseDTO> findAll(AuthenticatedUser authenticatedUser, Pageable pageable);

    @ApiOperation(value = "Rental delete operation")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success rent exclusion"),
            @ApiResponse(code = 404, message = "Rental not found error")
    })
    void delete(Long id, AuthenticatedUser authenticatedUser);

    @ApiOperation(value = "Rental update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success rent updated"),
            @ApiResponse(code = 400, message = "Missing required field, or an error on validation field rules")
    })
    RentalResponseDTO update(Long id, AuthenticatedUser authenticatedUser, RentalRequestUpdateDTO rentalRequestUpdateDTO);
}
