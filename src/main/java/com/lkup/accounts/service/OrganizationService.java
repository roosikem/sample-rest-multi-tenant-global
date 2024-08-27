package com.lkup.accounts.service;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.exceptions.organization.OrganizationNotFoundException;
import com.lkup.accounts.exceptions.organization.OrganizationServiceException;
import com.lkup.accounts.repository.global.OrganizationRepository;
import com.lkup.accounts.repository.global.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public OrganizationService(OrganizationRepository organizationRepository, TeamRepository teamRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.organizationRepository = organizationRepository;
        this.teamRepository = teamRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public Organization createOrganization(Organization organization) {
        organization.setId(defaultUUIDGenerator.generateId());
        try {
            return organizationRepository.save(organization);
        } catch (Exception e) {
            logger.error("Error creating organization", e);
            throw new OrganizationServiceException("Error creating organization", e.getMessage());
        }
    }

    public long getTotalOrganizations() {
        return organizationRepository.count();
    }

    public Optional<Organization> findOrganizationById(String id) {
        return organizationRepository.findById(id);
    }

    public List<Organization> findAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> updateOrganization(Organization organization) {
        Assert.notNull(organization.getId(), "Organization ID cannot be null for update");

        Optional<Organization> existingOrganizationOptional = organizationRepository.findById(organization.getId());

        if (existingOrganizationOptional.isPresent()) {
            Organization existingOrganization = existingOrganizationOptional.get();

            if (organization.getName() != null) {
                existingOrganization.setName(organization.getName());
            }
            if (organization.getCountry() != null) {
                existingOrganization.setCountry(organization.getCountry());
            }
            /*if (organization.getTeams() != null) {
                existingOrganization.setTeams(organization.getTeams());

                for (Team team : organization.getTeams()) {
                    team.setOrganization(existingOrganization);
                    teamRepository.save(team);
                }
            }*/

            return Optional.of(organizationRepository.save(existingOrganization));
        } else {
            throw new OrganizationNotFoundException("Organization with id " + organization.getId() + " not found");
        }
    }

    public void deleteOrganization(String id) {
        try {
            organizationRepository.deleteById(id);
            logger.info("Organization with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting organization with id {}", id, e);
            throw new OrganizationServiceException("Error deleting organization", e.getMessage());
        }
    }

    public Optional<Organization> findByName(String name) {
        try {
            return organizationRepository.findByName(name);

        } catch (Exception e) {
            throw new OrganizationNotFoundException("Organization with name " + name + " not found");
        }
    }

    public void deleteAll() {
        try {
            organizationRepository.deleteAll();
        } catch (Exception e) {
            throw new OrganizationServiceException("Error deleting organization");
        }
    }
}
