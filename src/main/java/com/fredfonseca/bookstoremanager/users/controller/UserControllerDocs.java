package com.fredfonseca.bookstoremanager.users.controller;

import com.fredfonseca.bookstoremanager.users.dto.MessageDTO;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api("System users management")
public interface UserControllerDocs {

    @ApiOperation(value = "User creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success user creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or user already registered on system")
    })
    MessageDTO create(UserDTO userToCreateDTO);

    @ApiOperation(value = "User delete operation")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success user exclusion"),
            @ApiResponse(code = 404, message = "User with id not found in the System")
    })
    void delete(Long id);

    @ApiOperation(value = "User update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user updated"),
            @ApiResponse(code = 400, message = "Missing required field, or an error on validation field rules")
    })
    MessageDTO update(Long id, UserDTO userToUpdateDTO);

    @ApiOperation(value = "Find user by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user found"),
            @ApiResponse(code = 404, message = "User not found error code")
    })
    UserDTO findById(Long id);

    @ApiOperation(value = "List all registered users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return all registered users")
    })
    List<UserDTO> findAll();
}
