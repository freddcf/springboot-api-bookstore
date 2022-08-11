package com.fredfonseca.bookstoremanager.users.controller;

import com.fredfonseca.bookstoremanager.users.dto.MessageDTO;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController implements UserControllerDocs{

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@RequestBody @Valid UserDTO userToCreateDTO) {
        return userService.create(userToCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public MessageDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO userToUpdateDTO) {
        return userService.update(id, userToUpdateDTO);
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }
}
