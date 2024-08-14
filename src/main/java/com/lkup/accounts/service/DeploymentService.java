package com.lkup.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lkup.accounts.config.s3.AwsProperties;
import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.document.Configuration;
import com.lkup.accounts.document.Deployment;
import com.lkup.accounts.exceptions.aws.AwsS3UploadException;
import com.lkup.accounts.exceptions.deployment.DeploymentNotFoundException;
import com.lkup.accounts.exceptions.deployment.DeploymentServiceException;
import com.lkup.accounts.repository.custom.DeploymentCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import com.lkup.accounts.utilities.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeploymentService {
    private static final Logger logger = LoggerFactory.getLogger(DeploymentService.class);

    private final DeploymentCustomRepository deploymentRepository;
    private final ConfigurationService configurationService;
    private final AwsS3Service awsS3Service;
    private final AwsProperties awsProperties;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public DeploymentService(DeploymentCustomRepository deploymentRepository, AwsS3Service awsS3Service,
                             ConfigurationService configurationService,
                             DefaultUUIDGeneratorService defaultUUIDGenerator,
                             AwsProperties awsProperties) {
        this.deploymentRepository = deploymentRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
        this.awsS3Service = awsS3Service;
        this.configurationService = configurationService;
        this.awsProperties = awsProperties;
    }

    public List<Deployment> findAllDeployments() {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return deploymentRepository.findAllDeployments(queryCriteria);
    }

    public  Deployment createDeployment(Deployment deployment) {
        deployment.setId(defaultUUIDGenerator.generateId());
        try{
            return  deploymentRepository.save(deployment);
        }catch (Exception e) {
            logger.error("Error creating deployment", e);
            throw new DeploymentServiceException("Error creating deployment", e.getMessage());
        }
    }
    public Optional<Deployment> findDeploymentById(String id) {
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        return deploymentRepository.findDeploymentById(queryCriteria, id);
    }

    public Optional<Deployment> updateDeployment(Deployment deployment) {
        Assert.notNull(deployment.getId(), "Deployment ID cannot be null for update");
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTenantId(RequestContext.getRequestContext().getTenantId());
        queryCriteria.setTeamId(RequestContext.getRequestContext().getTeamId());
        Optional<Deployment> existingDeploymentOptional = deploymentRepository.findById(queryCriteria, deployment.getId());

        if (existingDeploymentOptional.isPresent()) {
            Deployment existingDeployment = existingDeploymentOptional.get();

            if (deployment.getDeploymentName() != null) {
                existingDeployment.setDeploymentName(deployment.getDeploymentName());
            }
            if (deployment.getStatus() != null) {
                existingDeployment.setStatus(deployment.getStatus());
            }
            if(deployment.getConfiguration() != null) {
                Optional<Configuration> configDB = configurationService.findConfigurationById(deployment.getConfiguration().getId());
                configDB.ifPresent(existingDeployment::setConfiguration);
            }

            return Optional.of(deploymentRepository.save(existingDeployment));
        } else {
            throw new DeploymentNotFoundException("Deployment with id " + deployment.getId() + " not found");
        }
    }

    public Optional<Deployment> publishConfiguration(Deployment deployment) {
        if(deployment.getStatus()) {
            String url = this.awsUpload(deployment);
            if(Objects.nonNull(url)) {
                deployment.setPublishConfigUrl(url);
                deploymentRepository.save(deployment);
                return Optional.of(deploymentRepository.save(deployment));
            }
        }

        return Optional.of(deployment);
    }

    public String awsUpload(Deployment deployment) {
        String url = null;
        if(Objects.nonNull(deployment.getConfiguration())) {
            Configuration configuration = deployment.getConfiguration();
            String publishUrl = deployment.getPublishConfigUrl();
            if(Objects.isNull(publishUrl) || publishUrl.isEmpty()) {
                publishUrl = createPublishUrl(configuration);
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode actualObj = mapper.readTree((String)configuration.getWidgetConfig());
                url = awsS3Service.uploadJsonData(publishUrl , mapper.writeValueAsString(actualObj));
            } catch (JsonProcessingException e) {
                throw new AwsS3UploadException("InValid json");
            }
        }

        return url;
    }

    private String createPublishUrl(Configuration configuration) {
        StringBuilder uriBuilder = new StringBuilder();
        if(Objects.nonNull(awsProperties.getDirectory()) && !awsProperties.getDirectory().isEmpty()) {
            uriBuilder.append(awsProperties.getDirectory()).append("/");
        }
        uriBuilder.append(configuration.getId()).append("_");
        Optional.ofNullable(configuration.getMarket()).ifPresent(uriBuilder::append);
        uriBuilder.append("_");
        Optional.ofNullable(configuration.getEnvironment()).ifPresent(uriBuilder::append);
        uriBuilder.append("_");
        Optional.ofNullable(configuration.getAppId()).ifPresent(uriBuilder::append);
        uriBuilder.append("_");
        Optional.ofNullable(configuration.getLanguage()).ifPresent(uriBuilder::append);
        uriBuilder.append(ApplicationConstants.TOBI_UI_CONFIG_FILE_EXTENSION);
        return uriBuilder.toString().replace(" ", "_");
    }

}
