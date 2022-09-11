package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.users.dto.MessageDTO;
import com.fredfonseca.bookstoremanager.users.dto.RecoverUserInfo;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RecoverUserDataService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public RecoverUserDataService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDTO recoverUserData(RecoverUserInfo recoverUserInfo) {
        Users foundUser = userRepository.findByUsernameAndEmail(recoverUserInfo.getUsername(), recoverUserInfo.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found!")));

        String newPassword = recoverUserInfo.getPassword();
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);

        String message = String.format("Sua nova senha é %s", newPassword);
        return MessageDTO
                .builder()
                .message(message)
                .build();
    }
}
