package com.fredfonseca.bookstoremanager.rentals.controller;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RentalControllerDocs {

    @ApiOperation(value = "Rental creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book rent creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or rent already registered on system")
    })
    RentalResponseDTO create(RentalRequestDTO rentalRequestDTO);

    @ApiOperation(value = "Find rent by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success rent found"),
            @ApiResponse(code = 404, message = "Rental not found error")
    })
    RentalResponseDTO findById(Long id);

    @ApiOperation(value = "List all rents operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success rental list found")
    })
    List<RentalResponseDTO> findAll();

    @ApiOperation(value = "Rental delete operation")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success rent exclusion"),
            @ApiResponse(code = 404, message = "Rental not found error")
    })
    void delete(Long id);
}
