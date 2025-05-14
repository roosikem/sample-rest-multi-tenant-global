package com.lkup.accounts.service;

import com.lkup.accounts.context.jwt.JwtUtils;
import com.lkup.accounts.document.User;
import com.lkup.accounts.repository.global.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.login.AccountLockedException;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    @Value("${security.inactivity.lock.minutes:129600}") // 90 days default
    private long inactivityLockMinutes;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(String username, String password) throws AccountLockedException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Lock account if last login was over inactivityLockMinutes ago
            if (user.getLastLoginDate() != null &&
                java.time.Duration.between(user.getLastLoginDate(), java.time.LocalDateTime.now()).toMinutes() >= inactivityLockMinutes) {
                user.setAccountLocked(true);
                user.setLockTime(java.time.LocalDateTime.now());
                user.setLockedReason("Account locked due to inactivity.");
                userRepository.save(user);
                throw new AccountLockedException("Your account is locked due to inactivity. Please contact admin.");
            }

            // If account is locked
            if (user.isAccountLocked()) {
                if (user.getLockTime() != null &&
                    java.time.Duration.between(user.getLockTime(), java.time.LocalDateTime.now()).toMinutes() >= 15) {
                    // Auto unlock
                    user.setAccountLocked(false);
                    user.setFailedLoginAttempts(0);
                    user.setLockTime(null);
                } else {
                    // return Optional.empty(); // still locked
                    throw new AccountLockedException("Your account is locked. Please contact admin to unlock.");
                }
            }

            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setFailedLoginAttempts(0);
                user.setLastLoginDate(java.time.LocalDateTime.now());
                userRepository.save(user);
                String token = jwtUtil.issueToken(username, user.getRole());
                return Optional.of(token);
            } else {
                int attempts = user.getFailedLoginAttempts() + 1;
                user.setFailedLoginAttempts(attempts);
                if (attempts >= 5) {
                    user.setAccountLocked(true);
                    user.setLockTime(java.time.LocalDateTime.now());
                    user.setLockedReason("Account locked due to too many failed login attempts.");
                }
                userRepository.save(user);
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
