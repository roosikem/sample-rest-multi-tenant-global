package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Country;
import com.lkup.accounts.dto.country.CountryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CountryMapper {

    public CountryDto convertCountryToDto(Country country) {
        if (country == null) {
            return null;
        }

        CountryDto countryDto = new CountryDto();
        if (country.getId() != null) {
            countryDto.setId(country.getId());
        }
        if (country.getName() != null) {
            countryDto.setName(country.getName());
        }
        if (country.getCode() != null) {
            countryDto.setCode(country.getCode());
        }
        return countryDto;
    }

    public Country convertDtoToCountry(CountryDto countryDto) {
        if (countryDto == null) {
            return null;
        }

        Country country = new Country();
        if (countryDto.getId() != null) {
            country.setId(countryDto.getId());
        }
        if (countryDto.getName() != null) {
            country.setName(countryDto.getName());
        }
        if (countryDto.getCode() != null) {
            country.setCode(countryDto.getCode());
        }
        return country;
    }

    public Iterable<CountryDto> convertCountrysToDtos(Iterable<Country> countrys) {
        if (countrys == null) {
            return null;
        }

        List<CountryDto> countryDtos = new ArrayList<>();
        for (Country country : countrys) {
            if (country != null) {
                countryDtos.add(convertCountryToDto(country));
            }
        }
        return countryDtos;
    }
}
