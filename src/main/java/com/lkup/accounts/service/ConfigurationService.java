package com.lkup.accounts.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.document.Configuration;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.configuration.ConfigurationNotFoundException;
import com.lkup.accounts.exceptions.configuration.ConfigurationServiceException;
import com.lkup.accounts.repository.custom.ConfigurationCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import com.lkup.accounts.repository.global.OrganizationRepository;
import com.lkup.accounts.repository.global.TeamRepository;
import com.lkup.accounts.utilities.ApplicationConstants;
import com.lkup.accounts.utilities.OrganizationTeamValidator;
import com.lkup.accounts.utilities.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    private final ConfigurationCustomRepository configurationRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;
    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;
    private final RoleChecker roleChecker;

    @Autowired
    private OrganizationTeamValidator organizationTeamValidator;

    public ConfigurationService(RoleChecker roleChecker, ConfigurationCustomRepository configurationRepository, DefaultUUIDGeneratorService defaultUUIDGenerator,
                                OrganizationRepository organizationRepository,
                                TeamRepository teamRepository) {
        this.roleChecker = roleChecker;
        this.configurationRepository = configurationRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
        this.organizationRepository = organizationRepository;
        this.teamRepository = teamRepository;
    }

    public List<Configuration> findAllConfigurations() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return configurationRepository.findAllConfigurations(queryCriteria);
    }

    public Configuration createConfiguration(Configuration configuration) {
        configuration.setId(defaultUUIDGenerator.generateId());
        configuration.setCreatedAt(new Date());
        Assert.notNull(configuration.getOrganizationId(), "Organization Id can not be empty");

        Assert.notNull(configuration.getTeamId(), "Team Id can not be empty");
        try {

            organizationTeamValidator.validateOrganizationTeam(configuration.getOrganizationId(), configuration.getTeamId());

            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(configuration.getOrganizationId());
            queryCriteria.setTeamId(configuration.getTeamId());

            Optional<Configuration> configurationDbName = configurationRepository.findByName(queryCriteria, configuration.getName());
            if (configurationDbName.isPresent())
                throw new BadRequestException("Configuration already exist with name" + configuration.getName());

            Optional<Organization> organizationDb = organizationRepository.findById(configuration.getOrganizationId());
            if (organizationDb.isEmpty())
                throw new BadRequestException("Organization not exist " + configuration.getOrganizationId());

            Optional<Team> teamDb = teamRepository.findById(configuration.getTeamId());
            if (teamDb.isEmpty())
                throw new BadRequestException("Team not exist " + configuration.getTeamId());

            configuration.setTeamId(teamDb.get().getId());
            configuration.setOrganizationId(organizationDb.get().getId());

            Object config = configuration.getWidgetConfig();
            if (Objects.nonNull(config)) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson((String) config, JsonObject.class);
                Optional.ofNullable(configuration.getAuthTokenUrl()).ifPresent(value ->
                        jsonObject.addProperty(ApplicationConstants.JSON_TOKEN_URL_KEY, value)
                );
                Optional.ofNullable(configuration.getHostUrl()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_HOST_URL_KEY, value));
                Optional.ofNullable(configuration.getAppId()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_APP_ID_KEY, value.getAppId()));
                Optional.ofNullable(configuration.getWidgetColor()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_APP_HEADER_COLOR, value));
                Object s = gson.toJson(jsonObject, Object.class);
                configuration.setWidgetConfig(s);
            }
            return configurationRepository.save(configuration);
        } catch (Exception e) {
            throw new ConfigurationServiceException("Error creating configuration", e);
        }
    }

    public Optional<Configuration> findConfigurationById(String id) {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return configurationRepository.findConfigurationById(queryCriteria, id);
    }

    public Optional<Configuration> updateConfiguration(Configuration configuration) {
        Assert.notNull(configuration.getId(), "Configuration ID cannot be null for update");
        try {
            String organizationId = configuration.getOrganizationId();
            String teamId = configuration.getTeamId();
            organizationTeamValidator.validateOrganizationTeam(configuration.getOrganizationId(), configuration.getTeamId());

            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.setTenantId(organizationId);
            queryCriteria.setTeamId(teamId);
            Optional<Configuration> existingConfigurationOptional = configurationRepository.findConfigurationById(queryCriteria, configuration.getId());

            if (existingConfigurationOptional.isPresent()) {

                Optional<Configuration> configurationDbName = configurationRepository.findByName(queryCriteria, configuration.getName());
                if (configurationDbName.isPresent() && !configurationDbName.get().getId().equals(configuration.getId()))
                    throw new BadRequestException("Configuration already exist with name" + configuration.getName());

                Optional<Organization> organizationDb = organizationRepository.findById(organizationId);
                if (organizationDb.isEmpty())
                    throw new BadRequestException("Organization not exist " + organizationId);

                Optional<Team> teamDb = teamRepository.findByIdAndOrganizationId(organizationId, teamId);
                if (teamDb.isEmpty())
                    throw new BadRequestException("Team not exist " + teamId);

                Configuration existingConfiguration = existingConfigurationOptional.get();
                Optional<String> name = Optional.ofNullable(configuration.getName());
                name.ifPresent(existingConfiguration::setName);
                Optional.ofNullable(configuration.getDescription()).ifPresent(existingConfiguration::setDescription);
                Optional<String> widgetColor = Optional.ofNullable(configuration.getWidgetColor());
                widgetColor.ifPresent(existingConfiguration::setWidgetColor);
                Optional.ofNullable(configuration.getAlignment()).ifPresent(existingConfiguration::setAlignment);
                Optional.ofNullable(configuration.getSideSpace()).ifPresent(existingConfiguration::setSideSpace);
                Optional.ofNullable(configuration.getBottomSpace()).ifPresent(existingConfiguration::setBottomSpace);
                Optional.ofNullable(configuration.getLauncherButtonVisibility()).ifPresent(existingConfiguration::setLauncherButtonVisibility);
                Optional.ofNullable(configuration.getWidgetConfig()).ifPresent(existingConfiguration::setWidgetConfig);
                Optional.ofNullable(configuration.getEnvironment()).ifPresent(existingConfiguration::setEnvironment);
                Optional.ofNullable(configuration.getAppId()).ifPresent(existingConfiguration::setAppId);
                Optional.ofNullable(configuration.getHostUrl()).ifPresent(existingConfiguration::setHostUrl);
                Optional.ofNullable(configuration.getAuthTokenUrl()).ifPresent(existingConfiguration::setAuthTokenUrl);
                Optional.ofNullable(configuration.getConfigUrl()).ifPresent(existingConfiguration::setConfigUrl);
                Optional.ofNullable(configuration.getLanguage()).ifPresent(existingConfiguration::setLanguage);

                existingConfiguration.setOrganizationId(organizationId);
                existingConfiguration.setTeamId(teamId);

                Object config = configuration.getWidgetConfig();
                if (Objects.nonNull(config)) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson((String) config, JsonObject.class);
                    Optional.ofNullable(configuration.getAuthTokenUrl()).ifPresent(value ->
                            jsonObject.addProperty(ApplicationConstants.JSON_TOKEN_URL_KEY, value)
                    );
                    Optional.ofNullable(configuration.getHostUrl()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_HOST_URL_KEY, value));
                    Optional.ofNullable(configuration.getAppId()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_APP_ID_KEY, value.getAppId()));
                    Optional.ofNullable(configuration.getWidgetColor()).ifPresent(value -> jsonObject.addProperty(ApplicationConstants.JSON_APP_HEADER_COLOR, value));
                    Object s = gson.toJson(jsonObject, Object.class);
                    configuration.setWidgetConfig(s);
                }
                return Optional.of(configurationRepository.save(existingConfiguration));
            } else {
                throw new ConfigurationNotFoundException("Configuration with id " + configuration.getId() + " not found");
            }
        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Configuration with id " + configuration.getId() + " not found");
        }
    }

    public Configuration updatePublishedConfiguration(Configuration configuration) {

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(configuration.getOrganizationId());
        queryCriteria.setTeamId(configuration.getTeamId());
        Optional<Configuration> existingConfigurationOptional = configurationRepository.findConfigurationById(queryCriteria, configuration.getId());

        if (existingConfigurationOptional.isPresent()) {
            Configuration existingConfiguration = existingConfigurationOptional.get();
            existingConfiguration.setStatus(configuration.getStatus());
            existingConfiguration.setConfigUrl(configuration.getConfigUrl());
            return configurationRepository.save(existingConfiguration);
        }
        throw new ConfigurationNotFoundException("Configuration with id " + configuration.getId() + " not found");
    }

}
