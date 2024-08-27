package com.lkup.accounts.service;

import com.lkup.accounts.document.APIKey;
import com.lkup.accounts.exceptions.apikey.APIKeyNotFoundException;
import com.lkup.accounts.exceptions.apikey.APIKeyServiceException;
import com.lkup.accounts.repository.tenant.APIKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class APIKeyService {

    private static final Logger logger = LoggerFactory.getLogger(APIKeyService.class);
    private final APIKeyRepository apiKeyRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public APIKeyService(APIKeyRepository apiKeyRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.apiKeyRepository = apiKeyRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public APIKey createAPIKey(APIKey apiKey) {
        apiKey.setId(defaultUUIDGenerator.generateId());
        apiKey.setClientId(defaultUUIDGenerator.generateId()); // fix in the future to generate it via a specific logic
        apiKey.setClientSecret(defaultUUIDGenerator.generateId() + "salt");
        try {
            return apiKeyRepository.save(apiKey);
        } catch (Exception e) {
            logger.error("Error creating API key", e);
            throw new APIKeyServiceException("Error creating API key", e.getMessage());
        }
    }

    public long getTotalAPIKeys() {
        return apiKeyRepository.count();
    }

    public Optional<APIKey> findAPIKeyById(String id) {
        return apiKeyRepository.findById(id);
    }

    public List<APIKey> findAllAPIKeys() {
        return apiKeyRepository.findAll();
    }

    public Optional<APIKey> updateAPIKey(APIKey apiKey) {
        Assert.notNull(apiKey.getId(), "API Key ID cannot be null for update");

        Optional<APIKey> existingAPIKeyOptional = apiKeyRepository.findById(apiKey.getId());

        if (existingAPIKeyOptional.isPresent()) {
            APIKey existingAPIKey = existingAPIKeyOptional.get();

            if (apiKey.getName() != null) {
                existingAPIKey.setName(apiKey.getName());
            }
            if (apiKey.getClientType() != null) {
                existingAPIKey.setClientType(apiKey.getClientType());
            }
            if (apiKey.getDescription() != null) {
                existingAPIKey.setDescription(apiKey.getDescription());
            }

            return Optional.of(apiKeyRepository.save(existingAPIKey));
        } else {
            throw new APIKeyNotFoundException("API key with id " + apiKey.getId() + " not found");
        }
    }

    public void deleteAPIKey(String id) {
        try {
            apiKeyRepository.deleteById(id);
            logger.info("API key with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting API key with id {}", id, e);
            throw new APIKeyServiceException("Error deleting API key", e.getMessage());
        }
    }
}
