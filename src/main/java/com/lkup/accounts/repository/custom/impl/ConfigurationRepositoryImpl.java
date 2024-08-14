package com.lkup.accounts.repository.custom.impl;

import com.lkup.accounts.document.Configuration;
import com.lkup.accounts.repository.custom.ConfigurationCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConfigurationRepositoryImpl implements ConfigurationCustomRepository {

    private final MongoTemplate mongoTemplate;

    public ConfigurationRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Configuration> findConfigurationById(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        Configuration deployment = mongoTemplate.findOne(query, Configuration.class);
        return Optional.ofNullable(deployment);
    }

    @Override
    public List<Configuration> findAllConfigurations(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Configuration.class);
    }

    @Override
    public Optional<Configuration> findByName(QueryCriteria queryCriteria, String name) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("name").is(name)
        );
        Query query = new Query(criteria);
        Configuration deployment = mongoTemplate.findOne(query, Configuration.class);
        return Optional.ofNullable(deployment);
    }

    @Override
    public long countAll(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
       return  mongoTemplate.count(query, Configuration.class);
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration) {
        return mongoTemplate.save(configuration);
    }

    @Override
    public <S extends Configuration> S save(S entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void deleteConfiguration(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Configuration.class);
    }
}
