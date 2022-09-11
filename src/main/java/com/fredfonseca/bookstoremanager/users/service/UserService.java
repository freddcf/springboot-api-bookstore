package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.rentals.repository.RentalRepository;
import com.fredfonseca.bookstoremanager.users.dto.*;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.enums.Role;
import com.fredfonseca.bookstoremanager.users.exception.*;
import com.fredfonseca.bookstoremanager.users.mapper.UserMapper;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import com.fredfonseca.bookstoremanager.utils.StringPattern;
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

    private StringPattern stringPattern;

    private final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private final String ROLE_USER = Role.USER.getDescription();

    @Autowired
    public UserService(UserRepository userRepository,
                       RentalRepository rentalRepository,
                       PasswordEncoder passwordEncoder,
                       StringPattern stringPattern) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.passwordEncoder = passwordEncoder;
        this.stringPattern = stringPattern;
    }

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyIfEmailExists(userToCreateDTO.getEmail());

        Users userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setRole(Role.valueOf(ROLE_USER));
        userToCreate.setName(stringPattern.onlyWordsPattern(userToCreate.getName()));
        userToCreate.setCity(stringPattern.onlyWordsPattern(userToCreate.getCity()));
        userToCreate.setAddress(stringPattern.basicPattern(userToCreate.getAddress()));

        Users createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO createAdmin(AdminDTO userToCreateDTO) {
        verifyIfEmailExists(userToCreateDTO.getEmail());
        String formatedUsername = userToCreateDTO.getUsername().replaceAll(" ", "").toLowerCase();
        verifyIfUsernameExists(formatedUsername);

        Users userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setUsername(formatedUsername);
        userToCreate.setRole(Role.valueOf(ROLE_ADMIN));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userToCreate.setName(stringPattern.onlyWordsPattern(userToCreate.getName()));
        userToCreate.setCity(stringPattern.onlyWordsPattern(userToCreate.getCity()));
        userToCreate.setAddress(stringPattern.basicPattern(userToCreate.getAddress()));

        Users createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO) {
        Users foundUser = verifyAndGetIfExists(id);
        if(!foundUser.getRole().equals(Role.valueOf(ROLE_USER))) {
            throw new InvalidCredentialsChange(ROLE_USER);
        }
        validateUserCredentialsChange(foundUser.getEmail(), userToUpdateDTO.getEmail());

        Users userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setId(foundUser.getId());
        userToUpdate.setRole(Role.valueOf(ROLE_USER));
        userToUpdate.setName(stringPattern.onlyWordsPattern(userToUpdate.getName()));
        userToUpdate.setCity(stringPattern.onlyWordsPattern(userToUpdate.getCity()));
        userToUpdate.setAddress(stringPattern.basicPattern(userToUpdate.getAddress()));

        Users updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public MessageDTO updateAdmin(Long id, AdminDTO userToUpdateDTO) {
        userToUpdateDTO.setUsername(userToUpdateDTO.getUsername().replaceAll(" ", "").toLowerCase());
        Users foundUser = verifyAndGetIfExists(id);
        if(!foundUser.getRole().equals(Role.valueOf(ROLE_ADMIN))) {
            throw new InvalidCredentialsChange(ROLE_ADMIN);
        }
        if(foundUser.getUsername().equals("admin")){
            userToUpdateDTO.setUsername("admin");
        }
        validateAdminCredentialsChange(foundUser.getEmail(), foundUser.getUsername(),
                userToUpdateDTO.getEmail(), userToUpdateDTO.getUsername());

        Users userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setId(foundUser.getId());
        userToUpdate.setRole(Role.valueOf(ROLE_ADMIN));
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        userToUpdate.setName(stringPattern.onlyWordsPattern(userToUpdate.getName()));
        userToUpdate.setCity(stringPattern.onlyWordsPattern(userToUpdate.getCity()));
        userToUpdate.setAddress(stringPattern.basicPattern(userToUpdate.getAddress()));

        Users updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(Long id) {
        Users userToDelete = verifyAndGetIfExists(id);

        if(userToDelete.getRole().getDescription().equals(ROLE_ADMIN)) {
            throw new InvalidCredentialsChange(ROLE_USER);
        }
        if (!rentalRepository.findByUsers(userToDelete).isEmpty()) {
            throw new DeleteDeniedException();
        }
        userRepository.deleteById(id);
    }

    public void deleteAdmin(Long id, AuthenticatedUser authenticatedUser) {
        Users userToDelete = verifyAndGetIfExists(id);
        verifyAndGetUserIfExists(authenticatedUser.getUsername());

        if(userToDelete.getRole().getDescription().equals(ROLE_USER)) {
            throw new InvalidCredentialsChange(ROLE_ADMIN);
        }
        if(userToDelete.getUsername().equals("admin")) {
            throw new DeleteDeniedException(userToDelete.getUsername());
        }
        if (!rentalRepository.findByUsers(userToDelete).isEmpty()) {
            throw new DeleteDeniedException();
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO findById(Long id) {
        Users foundUser = verifyAndGetIfExists(id);
        return userMapper.toDTO(foundUser);
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<UserResponseDTO> usersList = userRepository.findAll(pageable).map(userMapper::toDTO);
        if(usersList.isEmpty()) {
            generateDefaultAdmin();
        }
        return usersList;
    }

    private void generateDefaultAdmin() {
        AdminDTO admin = AdminDTO.builder()
                .name("admin")
                .email("admin@gmail.com")
                .city("None")
                .address("None")
                .username("admin")
                .password("admin")
                .build();
        createAdmin(admin);
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
