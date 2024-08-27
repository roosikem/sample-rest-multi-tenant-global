package com.lkup.accounts.repository.custom.impl;

import com.lkup.accounts.document.Deployment;
import com.lkup.accounts.repository.custom.DeploymentCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeploymentCustomRepositoryImpl implements DeploymentCustomRepository {

    private final MongoTemplate mongoTemplate;

    public DeploymentCustomRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Deployment> findDeploymentById(QueryCriteria queryCriteria, String id) {
        return findById(queryCriteria, id);
    }

    @Override
    public List<Deployment> findAllDeployments(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Deployment.class);
    }

    @Override
    public Optional<Deployment> findByDeploymentName(QueryCriteria queryCriteria, String name) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("name").is(name)
        );
        Query query = new Query(criteria);
        Deployment deployment = mongoTemplate.findOne(query, Deployment.class);
        return Optional.ofNullable(deployment);
    }

    @Override
    public long countAll(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return mongoTemplate.count(query, Deployment.class);
    }

    @Override
    public Deployment updateDeployment(Deployment deployment) {
        return mongoTemplate.save(deployment);
    }

    @Override
    public <S extends Deployment> S save(S entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void deleteDeployment(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Deployment.class);
    }

    @Override
    public Optional<Deployment> findById(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId()),
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        Deployment deployment = mongoTemplate.findOne(query, Deployment.class);
        return Optional.ofNullable(deployment);
    }
}
