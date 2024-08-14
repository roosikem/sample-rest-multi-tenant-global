package com.lkup.accounts.mapper;

import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.apikey.CreateAPIKeyDto;
import com.lkup.accounts.dto.apikey.UpdateAPIKeyRequestDto;
import com.lkup.accounts.document.APIKey;
import com.lkup.accounts.dto.appId.AppIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class APIKeyMapper {

    @Autowired
    private APPIdMapper appIdMapper;

    public APIKeyDto convertAPIKeyToDto(APIKey apiKey) {
        if (apiKey == null) {
            return null;
        }
        APIKeyDto apiKeyDto = new APIKeyDto();
        if (apiKey.getId() != null) {
            apiKeyDto.setId(apiKey.getId());
        }
        if (apiKey.getName() != null) {
            apiKeyDto.setName(apiKey.getName());
        }
        if (apiKey.getClientId() != null) {
            apiKeyDto.setClientId(apiKey.getClientId());
        }
        if (apiKey.getClientSecret() != null) {
            apiKeyDto.setClientSecret(apiKey.getClientSecret());
        }
        if (apiKey.getClientType() != null) {
            apiKeyDto.setClientType(apiKey.getClientType());
        }
        if (apiKey.getDescription() != null) {
            apiKeyDto.setDescription(apiKey.getDescription());
        }
        if (apiKey.getCreatedAt() != null) {
            apiKeyDto.setCreatedAt(apiKey.getCreatedAt());
        }
        if (apiKey.getUpdatedAt() != null) {
            apiKeyDto.setUpdatedAt(apiKey.getUpdatedAt());
        }

        return apiKeyDto;
    }

    public APIKey convertCreateDtoToAPIKey(CreateAPIKeyDto createAPIKeyDto) {
        if (createAPIKeyDto == null) {
            return null;
        }
        APIKey apiKey = new APIKey();
        if (createAPIKeyDto.getName() != null) {
            apiKey.setName(createAPIKeyDto.getName());
        }
        if (createAPIKeyDto.getClientType() != null) {
            apiKey.setClientType(createAPIKeyDto.getClientType());
        }
        if (createAPIKeyDto.getDescription() != null) {
            apiKey.setDescription(createAPIKeyDto.getDescription());
        }
        return apiKey;
    }

    public UpdateAPIKeyRequestDto convertAPIKeyToUpdateDto(APIKey apiKey) {
        if (apiKey == null) {
            return null;
        }
        UpdateAPIKeyRequestDto updateAPIKeyDto = new UpdateAPIKeyRequestDto();
        if (apiKey.getId() != null) {
            updateAPIKeyDto.setId(apiKey.getId());
        }
        if (apiKey.getName() != null) {
            updateAPIKeyDto.setName(apiKey.getName());
        }
        if (apiKey.getClientType() != null) {
            updateAPIKeyDto.setClientType(apiKey.getClientType());
        }
        if (apiKey.getDescription() != null) {
            updateAPIKeyDto.setDescription(apiKey.getDescription());
        }
        return updateAPIKeyDto;
    }

    public APIKey convertUpdateDtoToAPIKey(UpdateAPIKeyRequestDto updateAPIKeyRequestDto) {
        if (updateAPIKeyRequestDto == null) {
            return null;
        }
        APIKey apiKey = new APIKey();
        if (updateAPIKeyRequestDto.getId() != null) {
            apiKey.setId(updateAPIKeyRequestDto.getId());
        }
        if (updateAPIKeyRequestDto.getName() != null) {
            apiKey.setName(updateAPIKeyRequestDto.getName());
        }
        if (updateAPIKeyRequestDto.getClientType() != null) {
            apiKey.setClientType(updateAPIKeyRequestDto.getClientType());
        }
        if (updateAPIKeyRequestDto.getDescription() != null) {
            apiKey.setDescription(updateAPIKeyRequestDto.getDescription());
        }
        return apiKey;
    }

    public CreateAPIKeyDto convertAPIKeyToCreateDto(APIKey apiKey) {
        if (apiKey == null) {
            return null;
        }
        CreateAPIKeyDto createAPIKeyDto = new CreateAPIKeyDto();
        if (apiKey.getName() != null) {
            createAPIKeyDto.setName(apiKey.getName());
        }
        if (apiKey.getClientType() != null) {
            createAPIKeyDto.setClientType(apiKey.getClientType());
        }
        if (apiKey.getDescription() != null) {
            createAPIKeyDto.setDescription(apiKey.getDescription());
        }
        return createAPIKeyDto;
    }

    public List<APIKeyDto> convertAPIKeysToDtos(List<APIKey> apiKeys) {
        List<APIKeyDto> apiKeyDtos = new ArrayList<>();
        if (apiKeys != null) {
            for (APIKey apiKey : apiKeys) {
                apiKeyDtos.add(convertAPIKeyToDto(apiKey));
            }
        }
        return apiKeyDtos;
    }

    public List<String> extractIdsFromAPIKeys(List<APIKey> apiKeys) {
        List<String> ids = new ArrayList<>();
        if (apiKeys != null) {
            for (APIKey apiKey : apiKeys) {
                if (apiKey.getId() != null) {
                    ids.add(apiKey.getId());
                }
            }
        }
        return ids;
    }

    public List<APIKey> convertIdsToAPIKeys(List<String> ids) {
        if (ids == null) {
            return null;
        }
        List<APIKey> apiKeys = new ArrayList<>();
        for (String id : ids) {
            APIKey apiKey = new APIKey();
            apiKey.setId(id);
            apiKeys.add(apiKey);
        }
        return apiKeys;
    }
}
