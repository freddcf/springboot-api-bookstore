package com.fredfonseca.bookstoremanager.users.controller;

import com.fredfonseca.bookstoremanager.users.dto.*;
import com.fredfonseca.bookstoremanager.users.service.AuthenticationService;
import com.fredfonseca.bookstoremanager.users.service.RecoverUserDataService;
import com.fredfonseca.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController implements UserControllerDocs{

    private UserService userService;

    private AuthenticationService authenticationService;

    private RecoverUserDataService recoverUserDataService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, RecoverUserDataService recoverUserDataService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.recoverUserDataService = recoverUserDataService;
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
    public UserResponseDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping(value = "/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO createAdmin(@RequestBody @Valid AdminDTO userToCreateDTO) {
        return userService.createAdmin(userToCreateDTO);
    }

    @PutMapping("admin/{id}")
    public MessageDTO updateAdmin(@PathVariable Long id, @RequestBody @Valid AdminDTO userToUpdateDTO) {
        return userService.updateAdmin(id, userToUpdateDTO);
    }

    @PostMapping("/authenticate")
    public JwtResponse createAuthenticationToken(@RequestBody @Valid JwtRequest jwtRequest) {
        return authenticationService.createAuthenticationToken(jwtRequest);
    }

    @PostMapping("/recover")
    public MessageDTO recoverUserData(@RequestBody @Valid RecoverUserInfo recoverUserInfo) {
        return recoverUserDataService.recoverUserData(recoverUserInfo);
    }
}
