package com.lkup.accounts.service;

import com.lkup.accounts.repository.tenant.IdGeneratorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultUUIDGeneratorService implements IdGeneratorRepository {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
