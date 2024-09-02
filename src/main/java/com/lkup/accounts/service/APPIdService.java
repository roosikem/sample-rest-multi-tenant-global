package com.lkup.accounts.service;

import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.document.AppId;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.ServiceException;
import com.lkup.accounts.exceptions.apikey.APIKeyNotFoundException;
import com.lkup.accounts.exceptions.apikey.APIKeyServiceException;
import com.lkup.accounts.repository.custom.APPIdCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import com.lkup.accounts.utilities.OrganizationTeamValidator;
import com.lkup.accounts.utilities.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class APPIdService {

    private static final Logger logger = LoggerFactory.getLogger(APPIdService.class);
    private final APPIdCustomRepository appIdRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;
    private final OrganizationService organizationService;
    private final TeamService teamService;
    private final OrganizationTeamValidator organizationTeamValidator;

    public APPIdService(OrganizationTeamValidator organizationTeamValidator, APPIdCustomRepository appIdRepository, DefaultUUIDGeneratorService defaultUUIDGenerator,
                        OrganizationService organizationService, TeamService teamService) {
        this.appIdRepository = appIdRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
        this.organizationService = organizationService;
        this.teamService = teamService;
        this.organizationTeamValidator = organizationTeamValidator;
    }

    public AppId createAPPId(AppId appId) {
        appId.setId(defaultUUIDGenerator.generateId());
        try {
           String organizationId =  appId.getOrganizationId();
           String teamId = appId.getTeamId();
            if (Objects.isNull(organizationId) || Objects.isNull(organizationId))
                throw new BadRequestException("Invalid organization id ");

            if (Objects.isNull(teamId) || Objects.isNull(teamId))
                throw new BadRequestException("Invalid Team id ");

            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(organizationId);
            queryCriteria.setTeamId(teamId);

            Optional<AppId> existAppId = appIdRepository.validateExisting(queryCriteria, appId.getName(), appId.getAppId());
            if (existAppId.isPresent())
                throw new BadRequestException("App ID already exists with name or AppId " + appId.getName() + ", " + appId.getAppId());
            Optional<Organization> dbOrganization = organizationService.findOrganizationById(organizationId);
            if (dbOrganization.isEmpty())
                throw new BadRequestException("Wrong organization id " + organizationId);
            Optional<Team> dbTeam = teamService.findByIdAndOrganizationId(teamId, organizationId);
            if (dbTeam.isEmpty())
                throw new BadRequestException("Wrong Team id " + teamId);
            appId.setTeamId(dbTeam.get().getId());
            appId.setOrganizationId(dbOrganization.get().getId());
            return appIdRepository.save(appId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public long getTotalAppIds() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return appIdRepository.count(queryCriteria);
    }

    public Optional<AppId> findAppIdById(String id) {
        QueryCriteria queryCriteria = new QueryCriteria();
        if(!organizationTeamValidator.isSuperAdmin()) {
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
            return appIdRepository.findByIdAndOrgTeam(queryCriteria, id);
        }
        return appIdRepository.findById(id);
    }

    public List<AppId> findAllAppIdsKeys() {
        QueryCriteria queryCriteria = new QueryCriteria();
        if (organizationTeamValidator.isSuperAdmin()) {
            return appIdRepository.findAllAppIds(queryCriteria);
        } else {
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
            return appIdRepository.findByTenantAndTeamId(queryCriteria);
        }
    }

    public Optional<AppId> updateAppId(AppId appId) {
        Assert.notNull(appId.getId(), "APP ID cannot be null for update");
        String organizationId =  appId.getOrganizationId();
        String teamId = appId.getTeamId();
        if (Objects.isNull(organizationId) || Objects.isNull(organizationId))
            throw new BadRequestException("Invalid organization id ");

        if (Objects.isNull(teamId) || Objects.isNull(teamId))
            throw new BadRequestException("Invalid Team id ");

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(organizationId);
        queryCriteria.setTeamId(teamId);
        Optional<AppId> existingAPIKeyOptional = appIdRepository.findById(appId.getId());
        Optional<AppId> existAppId = appIdRepository.validateExisting(queryCriteria, appId.getName(), appId.getAppId());

        if (existingAPIKeyOptional.isPresent()) {
            AppId existingAppId = existingAPIKeyOptional.get();
            if (existAppId.isPresent() && !existAppId.get().getId().equals(existingAppId.getId())) {
                throw new BadRequestException("Error creating App ID", "App ID already exists with name or AppId " + appId.getName() + ", " + appId.getAppId());
            }
            if (appId.getName() != null) {
                existingAppId.setName(appId.getName());
            }
            if (appId.getDescription() != null) {
                existingAppId.setDescription(appId.getDescription());
            }

            Optional<Organization> dbOrganization = organizationService.findOrganizationById(organizationId);
            if (dbOrganization.isEmpty())
                throw new BadRequestException("Wrong organization id " + organizationId);
            Optional<Team> dbTeam = teamService.findByIdAndOrganizationId(teamId, organizationId);
            if (dbTeam.isEmpty())
                throw new BadRequestException("Wrong Team id " + teamId);
            existingAppId.setTeamId(dbTeam.get().getId());
            existingAppId.setOrganizationId(dbOrganization.get().getId());
            return Optional.of(appIdRepository.save(existingAppId));
        } else {
            throw new APIKeyNotFoundException("APP ID with id " + appId.getId() + " not found");
        }
    }

    public void deleteAppIdKey(String id) {
        try {
            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
            appIdRepository.deleteById(queryCriteria, id);
            logger.info("APP ID with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting APP ID with id {}", id, e);
            throw new APIKeyServiceException("Error deleting APP ID", e.getMessage());
        }
    }

    public void deleteAll() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        appIdRepository.deleteAll(queryCriteria);
    }
}
