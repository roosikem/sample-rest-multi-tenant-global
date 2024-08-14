package com.lkup.accounts.controller;

import com.lkup.accounts.document.Country;
import com.lkup.accounts.dto.country.CountryDto;
import com.lkup.accounts.mapper.CountryMapper;
import com.lkup.accounts.service.CountryService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
    private final CountryMapper countryMapper;

    public CountryController(CountryService countryService, CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_COUNTRY + "', '" + PermissionConstants.CREATE_COUNTRY + "')")
    public ResponseEntity<Iterable<CountryDto>> getAllCountries() {
        List<Country> countries = countryService.findAllCountries();
        return ResponseEntity.ok(countryMapper.convertCountrysToDtos(countries));
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_COUNTRY + "', '" + PermissionConstants.CREATE_COUNTRY + "')")
    public ResponseEntity<Long> getTotalCountries() {
        return ResponseEntity.ok(countryService.getTotalCountries());
    }
}
