package com.lkup.accounts.repository.tenant;

import com.lkup.accounts.document.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository extends MongoRepository<Configuration, String> {

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<Configuration> findConfigurationById(String id);

    @Query(value = "{ 'organization.id' : ?#{T(com.lkup.accounts.context.RequestContext).getRequestContext().getTenantId()} " +
            ", 'team.id' : ?#{T(com.lkup.accounts.context.RequestContext).getRequestContext().getTeamId()} }")
    List<Configuration> findAllConfigurations();

    Optional<Configuration> findByName(String name);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Configuration updateConfiguration(Configuration configuration);

    @Override
    <S extends Configuration> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteConfiguration(String id);
}
