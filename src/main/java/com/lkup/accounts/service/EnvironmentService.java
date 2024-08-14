package com.lkup.accounts.service;

import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.document.AppId;
import com.lkup.accounts.document.Environment;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.environment.EnvironmentNotFoundException;
import com.lkup.accounts.exceptions.environment.EnvironmentServiceException;
import com.lkup.accounts.exceptions.organization.OrganizationNotFoundException;
import com.lkup.accounts.exceptions.team.TeamNotFoundException;
import com.lkup.accounts.repository.global.OrganizationRepository;
import com.lkup.accounts.repository.tenant.TeamRepository;
import com.lkup.accounts.repository.custom.APPIdCustomRepository;
import com.lkup.accounts.repository.custom.EnvironmentCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EnvironmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentService.class);
    private final EnvironmentCustomRepository environmentRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;
    private final OrganizationRepository organizationRepository;
    private final APPIdCustomRepository appIdRepository;
    private final TeamRepository teamRepository;

    public EnvironmentService(EnvironmentCustomRepository environmentRepository, DefaultUUIDGeneratorService defaultUUIDGenerator,
                              OrganizationRepository organizationRepository, APPIdCustomRepository appIdRepository, TeamRepository teamRepository) {
        this.environmentRepository = environmentRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
        this.organizationRepository = organizationRepository;
        this.appIdRepository = appIdRepository;
        this.teamRepository = teamRepository;
    }

    public Environment createEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        Objects.requireNonNull(environment.getOrganization());
        environment.setId(defaultUUIDGenerator.generateId());
        try {
            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());

            Organization organization = organizationRepository.findById(environment.getOrganization().getId())
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + environment.getOrganization().getId() + " not found"));
            environment.setOrganization(organization);

            Team team = teamRepository.findTeamById(RequestContext.getRequestContext().getTeamId()).orElseThrow(() -> new TeamNotFoundException("Team not found with id "+ RequestContext.getRequestContext().getTeamId()));
            Optional<Environment> existingEnv  = environmentRepository.findByName(queryCriteria, environment.getName());
            if(existingEnv.isPresent()) {
                throw new BadRequestException("Environment already exists with name "+environment.getName());
            }

            environment.setTeam(team);

            if(environment.getAppIds() != null) {
               List<String> appIds = environment.getAppIds().stream().map(AppId::getId).filter(id -> !id.isEmpty()).toList();

               Optional<List<AppId>> dbAppIds =  appIdRepository.findByIds(queryCriteria, appIds);
               dbAppIds.ifPresent(environment::setAppIds);
            }

            return environmentRepository.save(environment);
        } catch (Exception e) {
            logger.error("Error creating Environment", e);
            throw e;
        }
    }

    public long getTotalEnvironments() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return environmentRepository.countAll(queryCriteria);
    }

    public Optional<Environment> findEnvironmentById(String id) {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return environmentRepository.findById(queryCriteria, id);
    }

    public List<Environment> findAllEnvironments() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return environmentRepository.findAll(queryCriteria);
    }

    public Optional<Environment> updateEnvironment(Environment environment) {
        Assert.notNull(environment.getId(), "Environment ID cannot be null for update");
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        Optional<Environment> existingEnvironmentOptional = environmentRepository.findById(queryCriteria, environment.getId());

        if (existingEnvironmentOptional.isPresent()) {
            Environment existingEnvironment = existingEnvironmentOptional.get();

            if (environment.getName() != null) {
                existingEnvironment.setName(environment.getName());
            }

            if(environment.getApiKeys() != null) {
                existingEnvironment.setApiKeys(environment.getApiKeys());
            }

            if(environment.getOrganization() != null) {
                Organization organization = organizationRepository.findById(environment.getOrganization().getId())
                        .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + environment.getOrganization().getId() + " not found"));
                existingEnvironment.setOrganization(organization);
            }

            if(environment.getAppIds() != null) {
                List<String> appIds = environment.getAppIds().stream().map(AppId::getId).toList();
                Optional<List<AppId>> dbAppIds =  appIdRepository.findByIds(queryCriteria, appIds);
                dbAppIds.ifPresent(existingEnvironment::setAppIds);
            }

            return Optional.of(environmentRepository.save(existingEnvironment));
        } else {
            throw new EnvironmentNotFoundException("Environment with id " + environment.getId() + " not found");
        }
    }

    public void deleteEnvironment(String id) {
        try {
            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
            environmentRepository.deleteById(queryCriteria, id);
            logger.info("Environment with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting Environment with id {}", id, e);
            throw new EnvironmentServiceException("Error deleting Environment", e.getMessage());
        }
    }
}
