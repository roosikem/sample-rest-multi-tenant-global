package com.lkup.accounts.service;

import com.lkup.accounts.document.Role;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.document.User;
import com.lkup.accounts.exceptions.user.UserNotFoundException;
import com.lkup.accounts.exceptions.user.UserServiceException;
import com.lkup.accounts.repository.global.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public UserService(UserRepository userRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.userRepository = userRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public boolean allUsersExist(List<String> userIds) {
        for (String userId : userIds) {
            if (!userRepository.existsById(userId)) {
                return false;
            }
        }
        return true;
    }

    public User createUser(User user, Role role) {
        user.setId(defaultUUIDGenerator.generateId());
        user.setRole(role);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            throw new UserServiceException("Error creating user", e.getMessage());
        }
    }


    public Optional<User> findUserById(String id) {
        Optional<User> user = userRepository.findUserById(id);
        List<Team> teams = user.get().getTeams();
        for(Team t: teams) {
            String n = t.getName();
            System.out.println(n);
        }
        return userRepository.findUserById(id);
    }

    public List<User> findAllUsers(){
        return userRepository.findAllUsers();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public Optional<User> updateUser(User user) {
        Assert.notNull(user.getId(), "User ID cannot be null for update");

        Optional<User> existingUserOptional = userRepository.findById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getOrganization() != null) {
                existingUser.setOrganization(user.getOrganization());
            }
            if (user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }
            if (user.getTeams() != null) {
                existingUser.setTeams(user.getTeams());
            }

            return Optional.of(userRepository.save(existingUser));
        } else {
            throw new UserNotFoundException("User with id " + user.getId() + " not found");
        }
    }


    public boolean deleteUser(String id) {
        try {
            userRepository.deleteUser(id);
            logger.info("User with id {} deleted successfully", id);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting user with id {}", id, e);
            throw new UserServiceException("Error deleting user", e.getMessage());
        }
    }
}


