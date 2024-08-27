package com.lkup.accounts.controller;

import com.lkup.accounts.document.Role;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.user.CreateUserDto;
import com.lkup.accounts.dto.user.UpdateUserRequestDto;
import com.lkup.accounts.dto.user.UserTeamsDto;
import com.lkup.accounts.mapper.UserMapper;
import com.lkup.accounts.service.RoleService;
import com.lkup.accounts.service.UserService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public UserController(UserService userService, UserMapper userMapper, RoleService roleService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_USER + "')")
    public ResponseEntity<UserTeamsDto> createUser(@RequestBody CreateUserDto createUserDto) {
        User user = userMapper.convertCreateDtoToUser(createUserDto);
        Optional<Role> role = roleService.findRoleById(createUserDto.getRoleId());

        if (role.isEmpty())
            role = roleService.findRoleById("bbc412cb-b430-47f1-8010-6ad4a27a0237");

        if (role.isPresent()) {
            User createdUser = userService.createUser(user, role.get());
            UserTeamsDto userTeamsDto = userMapper.convertUserToDto(createdUser);
            return ResponseEntity.created(URI.create("/api/v1/users/" + userTeamsDto.getId())).body(userTeamsDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_USER + "' ,'" + PermissionConstants.VIEW_USER + "')")
    public ResponseEntity<UserTeamsDto> getUserById(@PathVariable String id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(userMapper::convertUserToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_USER + "' ,'" + PermissionConstants.VIEW_USER + "')")
    public ResponseEntity<Iterable<UserTeamsDto>> getAllUsers() {
        Iterable<User> users = userService.findAllUsers();
        return ResponseEntity.ok(userMapper.convertUsersToDtos(users));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_USER + "' ,'" + PermissionConstants.UPDATE_USER + "')")
    public ResponseEntity<UserTeamsDto> updateUser(@PathVariable String id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        updateUserRequestDto.setId(id);
        Optional<User> updatedUser = userService.updateUser(userMapper.convertUpdateDtoToUser(updateUserRequestDto));
        return updatedUser.map(userMapper::convertUserToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_USER + "')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_USER + "' ,'" + PermissionConstants.VIEW_USER + "')")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(userService.getTotalUsers());
    }
}
