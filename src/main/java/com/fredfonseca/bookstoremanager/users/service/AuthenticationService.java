package com.fredfonseca.bookstoremanager.users.service;

import com.fredfonseca.bookstoremanager.users.dto.AuthenticatedUser;
import com.fredfonseca.bookstoremanager.users.dto.JwtRequest;
import com.fredfonseca.bookstoremanager.users.dto.JwtResponse;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import com.fredfonseca.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenManager jwtTokenManager;

    @Autowired
    @Lazy
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public JwtResponse createAuthenticationToken(JwtRequest jwtRequest) {
        String username = jwtRequest.getUsername();
        authenticate(username, jwtRequest.getPassword());

        UserDetails userDetails = this.loadUserByUsername(username);
        String token = jwtTokenManager.generateToken(userDetails);

        return JwtResponse.builder()
                .jwtToken(token)
                .build();
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username %s", username)));
        return new AuthenticatedUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole().getDescription()
        );
    }
}