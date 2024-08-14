package com.lkup.accounts.repository.custom;

import com.lkup.accounts.document.Deployment;

import java.util.List;
import java.util.Optional;

public interface DeploymentCustomRepository {

    Optional<Deployment> findDeploymentById(QueryCriteria queryCriteria, String id);

    List<Deployment> findAllDeployments(QueryCriteria queryCriteria);

    Optional<Deployment> findByDeploymentName(QueryCriteria queryCriteria, String name);

    long countAll(QueryCriteria queryCriteria);

    Deployment updateDeployment(Deployment deployment);

    <S extends Deployment> S save(S entity);

     void deleteDeployment(QueryCriteria queryCriteria, String id);

    Optional<Deployment> findById(QueryCriteria queryCriteria, String id);
}
