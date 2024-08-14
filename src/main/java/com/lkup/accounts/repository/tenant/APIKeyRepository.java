package com.lkup.accounts.repository.tenant;

import com.lkup.accounts.document.APIKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface APIKeyRepository extends MongoRepository<APIKey, String> {

    @Query(value = "{ 'name' : { $eq : ?0 } }")
    Optional<APIKey> findByName(String name);

    @Override
    <S extends APIKey> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteById(String id);

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<APIKey> findById(String id);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{}")
    List<APIKey> findAll();
}

