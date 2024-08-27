package com.lkup.accounts.repository.global;

import com.lkup.accounts.document.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    @Query(value = "{ 'name' : { $eq : ?0 } }")
    Optional<Organization> findByName(String name);

    @Query(value = "{}", count = true)
    long countAll();

    @Override
    <S extends Organization> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteById(String id);

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<Organization> findById(String id);

    @Query(value = "{}")
    List<Organization> findAll();
}
