package com.lkup.accounts.controller;

import com.lkup.accounts.document.User;
import com.lkup.accounts.mapper.UserMapper;
import com.lkup.accounts.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<String> token = authenticationService.authenticate(username, password);

        if (token.isPresent()) {
            return ResponseEntity.ok(Map.of("token",token.get()));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Optional<User> user = authenticationService.userInfo(authHeader);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.map(userMapper::convertUserToDto));
        } else {
            return ResponseEntity.status(401).body("Invalid user");
        }
    }
}
