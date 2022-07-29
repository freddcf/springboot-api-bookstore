package com.fredfonseca.bookstoremanager.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @ApiOperation(value = "Return an example of hello world")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success message return")
    })
    @GetMapping
    public String hello() {
        return "Hello there, I'm now running an example with PR!!";
    }
}
