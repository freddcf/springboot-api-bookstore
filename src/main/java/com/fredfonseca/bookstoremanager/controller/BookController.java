package com.fredfonseca.bookstoremanager.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Operation(summary = "Return", description = "Return an example of hello world")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success message return",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping
    public String hello() {
        return "Hello there, lets get it done!";
    }
}
