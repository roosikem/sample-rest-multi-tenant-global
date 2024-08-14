package com.lkup.accounts.repository.tenant;

import com.lkup.accounts.document.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeploymentRepository extends MongoRepository<Deployment, String> {

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<Deployment> findDeploymentById(String id);

    @Query(value = "{ 'organization.id' : ?#{T(com.lkup.accounts.context.RequestContext).getRequestContext().getTenantId()} " +
            ", 'team.id' : ?#{T(com.lkup.accounts.context.RequestContext).getRequestContext().getTeamId()} }")
    List<Deployment> findAllDeployments();

    Optional<Deployment> findByDeploymentName(String name);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Deployment updateDeployment(Deployment deployment);

    @Override
    <S extends Deployment> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteDeployment(String id);
}
