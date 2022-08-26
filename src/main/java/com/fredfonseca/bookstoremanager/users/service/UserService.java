package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.dto.*;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.enums.Role;
import com.fredfonseca.bookstoremanager.users.exception.*;
import com.fredfonseca.bookstoremanager.users.mapper.UserMapper;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.fredfonseca.bookstoremanager.users.utils.MessageDTOUtils.creationMessage;
import static com.fredfonseca.bookstoremanager.users.utils.MessageDTOUtils.updatedMessage;

@Service
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    private RentalRepository rentalRepository;

    private PasswordEncoder passwordEncoder;

    private final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private final String ROLE_USER = Role.USER.getDescription();

    @Autowired
    public UserService(UserRepository userRepository,
                       RentalRepository rentalRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyIfEmailExists(userToCreateDTO.getEmail());

        Users userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setRole(Role.valueOf(ROLE_USER));

        Users createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO createAdmin(AdminDTO userToCreateDTO) {
        verifyIfEmailExists(userToCreateDTO.getEmail());
        verifyIfUsernameExists(userToCreateDTO.getUsername());

        Users userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setRole(Role.valueOf(ROLE_ADMIN));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        Users createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO) {
        Users foundUser = verifyAndGetIfExists(id);
        if(!foundUser.getRole().equals(Role.valueOf(ROLE_ADMIN))) {
            throw new InvalidCredentialsChange(ROLE_USER);
        }
        validateUserCredentialsChange(foundUser.getEmail(), userToUpdateDTO.getEmail());

        Users userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setId(foundUser.getId());
        userToUpdate.setRole(Role.valueOf(ROLE_USER));
        Users updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public MessageDTO updateAdmin(Long id, AdminDTO userToUpdateDTO) {
        Users foundUser = verifyAndGetIfExists(id);
        if(!foundUser.getRole().equals(Role.valueOf(ROLE_ADMIN))) {
            throw new InvalidCredentialsChange(ROLE_ADMIN);
        }
        validateAdminCredentialsChange(foundUser.getEmail(), foundUser.getUsername(),
                userToUpdateDTO.getEmail(), userToUpdateDTO.getUsername());

        Users userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setId(foundUser.getId());
        userToUpdate.setRole(Role.valueOf(ROLE_ADMIN));
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        Users updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {
        Users foundAuthenticatedUser = verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Users userToDelete = verifyAndGetIfExists(id);

        if (!rentalRepository.findByUsers(userToDelete).isEmpty()) {
            throw new DeleteDeniedException();
        }
        if (userToDelete.equals(foundAuthenticatedUser)) {
            throw new DeleteDeniedException(foundAuthenticatedUser.getName(), foundAuthenticatedUser.getRole().getDescription());
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO findById(Long id) {
        Users foundUser = verifyAndGetIfExists(id);
        return userMapper.toDTO(foundUser);
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    public Users verifyAndGetIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void validateUserCredentialsChange(String currEmail, String newEmail) {
        if (!currEmail.equals(newEmail)) {
            verifyIfEmailExists(newEmail);
        }
    }

    private void validateAdminCredentialsChange(String currEmail, String currUsername, String newEmail, String newUsername) {
        if (!currEmail.equals(newEmail) && !currUsername.equals(newUsername)) {
            throw new InvalidCredentialsChange();
        }
        if (!currEmail.equals(newEmail)) {
            verifyIfEmailExists(newEmail);
        }
        if (!currUsername.equals(newUsername)) {
            verifyIfUsernameExists(newUsername);
        }
    }

    private void verifyIfEmailExists(String email) {
        Optional<Users> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            throw new UserEmailAlreadyExistsException(email);
        }
    }

    private void verifyIfUsernameExists(String username) {
        Optional<Users> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }
    }

    public Users verifyAndGetUserIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
