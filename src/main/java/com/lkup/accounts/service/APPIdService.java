package com.lkup.accounts.service;
import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.document.*;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.ServiceException;
import com.lkup.accounts.exceptions.apikey.APIKeyNotFoundException;
import com.lkup.accounts.exceptions.apikey.APIKeyServiceException;
import com.lkup.accounts.repository.custom.APPIdCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class APPIdService {

    private static final Logger logger = LoggerFactory.getLogger(APPIdService.class);
    private final APPIdCustomRepository appIdRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;
    private final OrganizationService organizationService;
    private final TeamService teamService;

    public APPIdService(APPIdCustomRepository appIdRepository, DefaultUUIDGeneratorService defaultUUIDGenerator,
                        OrganizationService organizationService, TeamService teamService) {
        this.appIdRepository = appIdRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
        this.organizationService = organizationService;
        this.teamService = teamService;
    }

    public AppId createAPPId(AppId appId) {
        appId.setId(defaultUUIDGenerator.generateId());
        try {
            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
            queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());

           Optional<AppId> existAppId = appIdRepository.validateExisting(queryCriteria, appId.getName(), appId.getAppId());
           if(existAppId.isPresent())
               throw new BadRequestException("Error creating App ID", "App ID already exists with name or AppId "+appId.getName()+ ", " + appId.getAppId());
           String tenantId =  RequestContext.getRequestContext().getTenantId();
           Optional<Organization> dbOrganization = organizationService.findOrganizationById(tenantId);
           if(dbOrganization.isEmpty())
               throw new BadRequestException("Wrong organization id "+ tenantId);
           String teamId =  RequestContext.getRequestContext().getTeamId();
           Optional<Team> dbTeam =  teamService.findTeamById(teamId);
            if(dbTeam.isEmpty())
                throw new BadRequestException("Wrong Team id "+ teamId);
            appId.setTeam(dbTeam.get());
            appId.setOrganization(dbOrganization.get());
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
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return appIdRepository.findById(queryCriteria, id);
    }

    public List<AppId> findAllAppIdsKeys() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return appIdRepository.findAllAppIds(queryCriteria);
    }

    public Optional<AppId> updateAppId(AppId apiKey) {
        Assert.notNull(apiKey.getId(), "APP ID cannot be null for update");
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        Optional<AppId> existingAPIKeyOptional = appIdRepository.findById(queryCriteria, apiKey.getId());
        Optional<AppId> existAppId = appIdRepository.validateExisting(queryCriteria, apiKey.getName(), apiKey.getAppId());

        if (existingAPIKeyOptional.isPresent()) {
            AppId existingAppId = existingAPIKeyOptional.get();
            if (existAppId.isPresent() && !existAppId.get().getId().equals(existingAppId.getId())) {
                throw new BadRequestException("Error creating App ID", "App ID already exists with name or AppId "+apiKey.getName()+ ", " + apiKey.getAppId());
            }
            if (apiKey.getName() != null) {
                existingAppId.setName(apiKey.getName());
            }
            if (apiKey.getDescription() != null) {
                existingAppId.setDescription(apiKey.getDescription());
            }

            String tenantId =  RequestContext.getRequestContext().getTenantId();
            Optional<Organization> dbOrganization = organizationService.findOrganizationById(tenantId);
            if(dbOrganization.isEmpty())
                throw new BadRequestException("Wrong organization id "+ tenantId);
            String teamId =  RequestContext.getRequestContext().getTeamId();
            Optional<Team> dbTeam =  teamService.findTeamById(teamId);
            if(dbTeam.isEmpty())
                throw new BadRequestException("Wrong Team id "+ tenantId);
            existingAppId.setTeam(dbTeam.get());
            existingAppId.setOrganization(dbOrganization.get());
            return Optional.of(appIdRepository.save(existingAppId));
        } else {
            throw new APIKeyNotFoundException("APP ID with id " + apiKey.getId() + " not found");
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
