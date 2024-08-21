package com.lkup.accounts.repository.global;

import com.lkup.accounts.document.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

    @Query(value = "{ '_id' : ?0 }")
    Optional<Team> findTeamById(String id);

    @Query(value = "{}", count = true)
    long countAll();

    @Query(value = "{}")
    List<Team> findAllTeams();

    @Query(value = "{ 'organization._id' : ?0 }")
    List<Team> findTeamsByOrganizationId(String organizationId);

    @Override
    <S extends Team> S save(S entity);

    @Query(value = "{ '_id' : ?0 }", delete = true)
    void deleteTeam(String id);
}
