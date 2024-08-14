package com.lkup.accounts.service;

import com.lkup.accounts.document.User;
import com.lkup.accounts.context.jwt.JwtUtils;
import com.lkup.accounts.repository.global.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.issueToken(username, user.getRole());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public Optional<User> userInfo(String authHeader) {
        Objects.requireNonNull(authHeader);
        String username = jwtUtil.getSubject(authHeader);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
