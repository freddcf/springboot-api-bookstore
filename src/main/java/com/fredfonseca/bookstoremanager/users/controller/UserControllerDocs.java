package com.fredfonseca.bookstoremanager.users.controller;

import com.fredfonseca.bookstoremanager.users.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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
    void delete(Long id, AuthenticatedUser authenticatedUser);

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
    UserResponseDTO findById(Long id);

    @ApiOperation(value = "List all registered users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return all registered users")
    })
    Page<UserResponseDTO> findAll(Pageable pageable);

    @ApiOperation(value = "Admin creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success user creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or user already registered on system")
    })
    MessageDTO createAdmin(AdminDTO userToCreateDTO);

    @ApiOperation(value = "Admin update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user updated"),
            @ApiResponse(code = 400, message = "Missing required field, or an error on validation field rules")
    })
    MessageDTO updateAdmin(Long id, AdminDTO userToUpdateDTO);

    @ApiOperation(value = "User authentication operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user authenticated"),
            @ApiResponse(code = 404, message = "User not found")
    })
    JwtResponse createAuthenticationToken(JwtRequest jwtRequest);

    @ApiOperation(value = "User recover data operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user found"),
            @ApiResponse(code = 404, message = "User not found")
    })
    MessageDTO recoverUserData(RecoverUserInfo recoverUserInfo);
}
