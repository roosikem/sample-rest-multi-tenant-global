package com.lkup.accounts.repository.global;

import com.lkup.accounts.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    Optional<User> findUserById(String id);

    @Query(value = "{}")
    List<User> findAllUsers();

    Optional<User> findByUsername(String username);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{ '_id' : { $eq : ?0 } }")
    User updateUser(User user);

    @Override
    <S extends User> S save(S entity);

    @Query(value = "{ '_id' : { $eq : ?0 } }", delete = true)
    void deleteUser(String id);
}
