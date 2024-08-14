package com.lkup.accounts.repository.custom;

import com.lkup.accounts.document.Environment;

import java.util.List;
import java.util.Optional;

public interface EnvironmentCustomRepository {
    Optional<Environment> findByName(QueryCriteria queryCriteria, String name);

    <S extends Environment> S save(S entity);

    void deleteById(QueryCriteria queryCriteria, String id);

    Optional<Environment> findById(QueryCriteria queryCriteria, String id);

     long countAll(QueryCriteria queryCriteria);

    List<Environment> findAll(QueryCriteria queryCriteria );
}
