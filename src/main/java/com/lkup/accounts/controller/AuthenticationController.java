package com.lkup.accounts.controller;

import com.lkup.accounts.document.User;
import com.lkup.accounts.mapper.UserMapper;
import com.lkup.accounts.service.AuthenticationService;
import com.lkup.accounts.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper,
                                    UserService userService) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            Optional<String> token = authenticationService.authenticate(username, password);
            if (token.isPresent()) {
                return ResponseEntity.ok(Map.of("token", token.get()));
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Objects.nonNull(authHeader)) {
            Optional<User> user = authenticationService.userInfo(authHeader);
            if (user.isPresent()) {
                Optional<User> userInfos = userService.findUserById(user.get().getId());
                if (userInfos.isPresent()) {
                    return ResponseEntity.ok(userInfos.map(userMapper::convertUserToDto));
                }
            }
        }

        return ResponseEntity.status(401).body("Invalid user");
    }
}

