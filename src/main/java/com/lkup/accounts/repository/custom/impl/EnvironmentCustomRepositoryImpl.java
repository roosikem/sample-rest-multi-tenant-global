package com.lkup.accounts.repository.custom.impl;

import com.lkup.accounts.document.Environment;
import com.lkup.accounts.repository.custom.EnvironmentCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EnvironmentCustomRepositoryImpl implements EnvironmentCustomRepository {

    private final MongoTemplate mongoTemplate;

    public EnvironmentCustomRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Environment> findByName(QueryCriteria queryCriteria, String name) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("name").is(name)
        );
        Query query = new Query(criteria);
        Environment environment = mongoTemplate.findOne(query, Environment.class);
        return Optional.ofNullable(environment);
    }

    @Override
    public <S extends Environment> S save(S entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void deleteById(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Environment.class);
    }

    @Override
    public Optional<Environment> findById(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        Environment environment = mongoTemplate.findOne(query, Environment.class);
        return Optional.ofNullable(environment);
    }

    @Override
    public long countAll(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return mongoTemplate.count(query, Environment.class);
    }

    @Override
    public List<Environment> findAll(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Environment.class);
    }
}
