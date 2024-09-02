package com.lkup.accounts.mapper;

import com.lkup.accounts.document.AppId;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.appId.CreateUpdateAppIdDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.service.OrganizationService;
import com.lkup.accounts.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class APPIdMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrganizationService organizationService;

    public AppId convertCreateDtoToAPPIdEntity(CreateUpdateAppIdDto createUpdateAppIdDto) {
        AppId appId = modelMapper.map(createUpdateAppIdDto, AppId.class);
        if (null != createUpdateAppIdDto.getTeam()) {
            appId.setTeamId(createUpdateAppIdDto.getTeam());
        }
        return modelMapper.map(createUpdateAppIdDto, AppId.class);
    }

    public CreateUpdateAppIdDto convertAppIdToCreateUpdateDto(AppId appId) {
        return modelMapper.map(appId, CreateUpdateAppIdDto.class);
    }

    public AppIdDto convertAPPIdToDto(AppId appId) {
         return modelMapper.map(appId, AppIdDto.class);
    }

    public List<AppIdDto> convertAPPIdsToDtos(List<AppId> appIds) {
        List<AppIdDto> appIdsList = new ArrayList<>();
        if (appIds != null) {
            for (AppId appId : appIds) {
                appIdsList.add(convertAPPIdToDto(appId));
            }
        }
        return appIdsList;
    }

    public AppId convertAPPIdDtoToAppId(AppIdDto appIdDto) {
        return modelMapper.map(appIdDto, AppId.class);
    }

    public List<AppId> convertAPPIdsDtoToAppId(List<AppIdDto> appIds) {
        List<AppId> appIdsList = new ArrayList<>();
        if (appIds != null) {
            for (AppIdDto appId : appIds) {
                appIdsList.add(convertAPPIdDtoToAppId(appId));
            }
        }
        return appIdsList;
    }

    public List<AppId> convertIdsToAppId(List<String> appIds) {
        List<AppId> appIdsList = new ArrayList<>();
        if (appIds != null) {
            for (String appId : appIds) {
                AppId appIdDoc = new AppId();
                appIdDoc.setId(appId);
                appIdsList.add(appIdDoc);
            }
        }
        return appIdsList;
    }
}
