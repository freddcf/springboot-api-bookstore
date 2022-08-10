package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.exception.DeleteDeniedException;
import com.fredfonseca.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.fredfonseca.bookstoremanager.users.exception.UserNotFoundException;
import com.fredfonseca.bookstoremanager.users.mapper.UserMapper;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    private RentalRepository rentalRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,RentalRepository rentalRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO create(UserDTO userToCreateDTO) {
        verifyIfExists(userToCreateDTO.getEmail());
        Users userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        Users createdUser = userRepository.save(userToCreate);
        return userMapper.toDTO(createdUser);
    }

    public UserDTO update(Long id, UserDTO userToUpdateDTO) {
        Users foundUser = verifyAndGetIfExists(id);

        userToUpdateDTO.setId(foundUser.getId());
        Users userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));

        Users updatedUser = userRepository.save(userToUpdate);
        return userMapper.toDTO(updatedUser);
    }

    public void delete(Long id) {
        Users userToDelete = verifyAndGetIfExists(id);
        if(rentalRepository.findByUsers(userToDelete).isPresent()) throw new DeleteDeniedException();
        userRepository.deleteById(id);
    }

    public UserDTO findById(Long id) {
        Users foundUser = verifyAndGetIfExists(id);
        return userMapper.toDTO(foundUser);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Users verifyAndGetIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(String email) {
        Optional<Users> foundUser = userRepository.findByEmail(email);
        if(foundUser.isPresent()) throw new UserAlreadyExistsException(email);
    }

}
