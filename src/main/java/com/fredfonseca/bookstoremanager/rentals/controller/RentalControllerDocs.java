package com.fredfonseca.bookstoremanager.rentals.controller;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface RentalControllerDocs {

    @ApiOperation(value = "Rental creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book rent creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or rent already registered on system")
    })
    RentalResponseDTO create(RentalRequestDTO rentalRequestDTO);
}
