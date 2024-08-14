package com.lkup.accounts.service;

import com.lkup.accounts.document.Role;
import com.lkup.accounts.exceptions.role.RoleNotFoundException;
import com.lkup.accounts.exceptions.role.RoleServiceException;
import com.lkup.accounts.repository.global.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public RoleService(RoleRepository roleRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.roleRepository = roleRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public Role createRole(Role role) {
        try {
            role.setId(defaultUUIDGenerator.generateId());
            return roleRepository.save(role);
        } catch (Exception e) {
            logger.error("Error creating role", e);
            throw new RoleServiceException("Error creating role");
        }
    }

    public long getTotalRoles() {
        return roleRepository.count();
    }

    public Optional<Role> findRoleById(String id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> updateRole(Role role) {
        Role existingRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RoleNotFoundException("Role with id " + role.getId() + " not found"));

        if (role.getName() != null) {
            existingRole.setName(role.getName());
        }
        if (role.getPermissions() != null) {
            existingRole.setPermissions(role.getPermissions());
        }

        return Optional.of(roleRepository.save(existingRole));
    }

    public boolean deleteRole(String id) {
        try {
            roleRepository.deleteById(id);
            logger.info("Role with id {} deleted successfully", id);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting role with id {}", id, e);
            throw new RoleServiceException("Error deleting role");
        }
    }
}
