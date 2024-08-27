package com.lkup.accounts.service;

import com.lkup.accounts.document.Country;
import com.lkup.accounts.repository.global.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public CountryService(CountryRepository countryRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.countryRepository = countryRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public long getTotalCountries() {
        return countryRepository.count();
    }

    public Optional<Country> findCountryById(String id) {
        return countryRepository.findById(id);
    }

    public Optional<Country> findCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    public Optional<Country> findCountryByCode(String code) {
        return countryRepository.findByCode(code);
    }

    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }


}
