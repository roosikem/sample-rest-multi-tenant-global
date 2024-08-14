package com.lkup.accounts.repository.global;

import com.lkup.accounts.document.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    @Query(value = "{ 'name' : { $eq : ?0 } }")
    Optional<Role> findByName(String name);

    @Query(value = "{}", count = true)
    long countAll();

    @Override
    <S extends Role> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteById(String id);

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<Role> findById(String id);

    @Query(value = "{}")
    List<Role> findAll();
}
