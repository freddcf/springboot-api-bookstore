package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.users.dto.MessageDTO;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.fredfonseca.bookstoremanager.users.mapper.UserMapper;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyIfExists(userToCreateDTO.getEmail(), userToCreateDTO.getUsername());
        Users userToCreate = userMapper.toModel(userToCreateDTO);
        Users createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    private void verifyIfExists(String email, String username) {
        Optional<Users> foundUser = userRepository.findByEmailOrUsername(email, username);
        if(foundUser.isPresent()) throw new UserAlreadyExistsException(email, username);
    }

    private MessageDTO creationMessage(Users createdUser) {
        String createdUserUsername = createdUser.getUsername();
        Long createdUserId = createdUser.getId();
        String createdUserMessage = String.format("User %s with ID %s successfully created", createdUserUsername, createdUserId);
        return MessageDTO.builder()
                .message(createdUserMessage)
                .build();
    }
}
