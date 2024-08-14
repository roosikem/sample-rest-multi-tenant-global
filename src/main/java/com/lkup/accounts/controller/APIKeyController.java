package com.lkup.accounts.controller;

import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.apikey.CreateAPIKeyDto;
import com.lkup.accounts.dto.apikey.UpdateAPIKeyRequestDto;
import com.lkup.accounts.document.APIKey;
import com.lkup.accounts.mapper.APIKeyMapper;
import com.lkup.accounts.service.APIKeyService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.lkup.accounts.utilities.PermissionConstants.CREATE_API_KEY;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyAuthority;

@RestController
@RequestMapping("/api/v1/keys")
public class APIKeyController{

    private final APIKeyService apiKeyService;
    private final APIKeyMapper apiKeyMapper;

    public APIKeyController(APIKeyService apiKeyService, APIKeyMapper apiKeyMapper) {
        this.apiKeyService = apiKeyService;
        this.apiKeyMapper = apiKeyMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<APIKeyDto> createAPIKey(@RequestBody CreateAPIKeyDto createAPIKeyDto) {
        APIKey apiKey = apiKeyMapper.convertCreateDtoToAPIKey(createAPIKeyDto);
        APIKey createdAPIKey = apiKeyService.createAPIKey(apiKey);
        APIKeyDto apiKeyDto = apiKeyMapper.convertAPIKeyToDto(createdAPIKey);
        return ResponseEntity.created(URI.create("/api/v1/api-keys/" + apiKeyDto.getId())).body(apiKeyDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<APIKeyDto> getAPIKeyById(@PathVariable String id) {
        Optional<APIKey> apiKey = apiKeyService.findAPIKeyById(id);
        return apiKey.map(apiKeyMapper::convertAPIKeyToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<Iterable<APIKeyDto>> getAllAPIKeys() {
        List<APIKey> apiKeys = apiKeyService.findAllAPIKeys();
        return ResponseEntity.ok(apiKeyMapper.convertAPIKeysToDtos(apiKeys));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<APIKeyDto> updateAPIKey(@PathVariable String id, @RequestBody UpdateAPIKeyRequestDto updateAPIKeyRequestDto) {
        updateAPIKeyRequestDto.setId(id);
        Optional<APIKey> updatedAPIKey = apiKeyService.updateAPIKey(apiKeyMapper.convertUpdateDtoToAPIKey(updateAPIKeyRequestDto));
        return updatedAPIKey.map(apiKeyMapper::convertAPIKeyToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_API_KEY + "')")
    public ResponseEntity<Void> deleteAPIKey(@PathVariable String id) {
        apiKeyService.deleteAPIKey(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<Long> getTotalAPIKeys() {
        return ResponseEntity.ok(apiKeyService.getTotalAPIKeys());
    }
}
