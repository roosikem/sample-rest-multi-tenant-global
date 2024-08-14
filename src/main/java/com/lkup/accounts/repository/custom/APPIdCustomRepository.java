package com.lkup.accounts.repository.custom;

import com.lkup.accounts.document.AppId;

import java.util.List;
import java.util.Optional;

public interface APPIdCustomRepository {
    Optional<AppId> validateExisting(QueryCriteria queryCriteria, String name, String appId);

    Optional<AppId> findByName(QueryCriteria queryCriteria, String name);

    <S extends AppId> S save(S entity);

    void deleteById(QueryCriteria queryCriteria, String id);

    Optional<AppId> findById(QueryCriteria queryCriteria, String id);

    long countAll(QueryCriteria queryCriteria);

    long count(QueryCriteria queryCriteria);

    List<AppId> findAllAppIds(QueryCriteria queryCriteria);

    Optional<List<AppId>> findByIds(QueryCriteria queryCriteria, List<String> ids);

    void deleteAll(QueryCriteria queryCriteria);
}
