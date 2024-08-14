package com.lkup.accounts.repository.custom;

import com.lkup.accounts.document.Configuration;

import java.util.List;
import java.util.Optional;

public interface ConfigurationCustomRepository {

    Optional<Configuration> findConfigurationById(QueryCriteria queryCriteria, String id);

    List<Configuration> findAllConfigurations(QueryCriteria queryCriteria);

    Optional<Configuration> findByName(QueryCriteria queryCriteria, String name);

    long countAll(QueryCriteria queryCriteria);

    Configuration updateConfiguration(Configuration configuration);

    <S extends Configuration> S save(S entity);

    void deleteConfiguration(QueryCriteria queryCriteria, String id);
}
