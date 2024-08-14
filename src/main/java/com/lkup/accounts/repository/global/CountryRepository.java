package com.lkup.accounts.repository.global;

import com.lkup.accounts.document.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<Country> findById(String id);

    @Query(value = "{ 'name' : { $eq : ?0 } }")
    Optional<Country> findByName(String name);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{ 'code' : { $eq : ?0 } }")
    Optional<Country> findByCode(String id);

    @Query(value = "{}")
    List<Country> findAll();
}
