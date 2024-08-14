package com.lkup.accounts.controller;

import com.lkup.accounts.document.APIKey;
import com.lkup.accounts.document.AppId;
import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.apikey.UpdateAPIKeyRequestDto;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.appId.CreateUpdateAppIdDto;
import com.lkup.accounts.mapper.APPIdMapper;
import com.lkup.accounts.service.APPIdService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/appIds")
public class APPIdController {

    private final APPIdService appIdService;
    private final APPIdMapper appIdMapper;

    public APPIdController(APPIdService appIdService, APPIdMapper appIdMapper) {
        this.appIdService = appIdService;
        this.appIdMapper = appIdMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<AppIdDto> getAPIKeyById(@PathVariable String id) {
        Optional<AppId> appId = appIdService.findAppIdById(id);
        return appId.map(appIdMapper::convertAPPIdToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<CreateUpdateAppIdDto> createAppId(@RequestBody CreateUpdateAppIdDto createAppIdDto) {
        AppId appId = appIdMapper.convertCreateDtoToAPPIdEntity(createAppIdDto);
        appId = appIdService.createAPPId(appId);
        CreateUpdateAppIdDto createUpdateAppIdDto = appIdMapper.convertAppIdToCreateUpdateDto(appId);
        return ResponseEntity.created(URI.create("/api/v1/appIds/" + createUpdateAppIdDto.getId())).body(createUpdateAppIdDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<AppIdDto> updateAppId(@PathVariable String id, @RequestBody CreateUpdateAppIdDto createUpdateAppIdDto) {
        createUpdateAppIdDto.setId(id);
        AppId appId = appIdMapper.convertCreateDtoToAPPIdEntity(createUpdateAppIdDto);
        Optional<AppId> updatedAPIKey = appIdService.updateAppId(appId);
        return updatedAPIKey.map(appIdMapper::convertAPPIdToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_API_KEY + "', '" + PermissionConstants.CREATE_API_KEY + "')")
    public ResponseEntity<Iterable<AppIdDto>> getAllAppId() {
        List<AppId> apiKeys = appIdService.findAllAppIdsKeys();
        return ResponseEntity.ok(appIdMapper.convertAPPIdsToDtos(apiKeys));
    }
}
