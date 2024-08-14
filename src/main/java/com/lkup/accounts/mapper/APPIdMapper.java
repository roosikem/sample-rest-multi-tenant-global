package com.lkup.accounts.mapper;

import com.lkup.accounts.document.APIKey;
import com.lkup.accounts.document.AppId;
import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.apikey.CreateAPIKeyDto;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.appId.CreateUpdateAppIdDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class APPIdMapper {

    @Autowired
    private ModelMapper modelMapper;

    public AppId convertCreateDtoToAPPIdEntity(CreateUpdateAppIdDto createUpdateAppIdDto) {
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
}
