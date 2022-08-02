package com.fredfonseca.bookstoremanager.author.controller;

import com.fredfonseca.bookstoremanager.author.dto.AuthorDTO;
import com.fredfonseca.bookstoremanager.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@CrossOrigin(origins = "*")
public class AuthorController implements AuthorControllerDocs{

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create(@RequestBody @Valid AuthorDTO authorDTO) {
        return authorService.create(authorDTO);
    }

    @GetMapping("/{id}")
    public AuthorDTO findById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @GetMapping
    public List<AuthorDTO> findAll() {
        return authorService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }

    @PutMapping("/{id}")
    public AuthorDTO update(@PathVariable Long id, @RequestBody @Valid AuthorDTO authorDTO) {
        return authorService.update(id, authorDTO);
    }
}
