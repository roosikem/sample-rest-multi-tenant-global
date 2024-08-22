package com.lkup.accounts.repository.custom.impl;

import com.lkup.accounts.document.AppId;
import com.lkup.accounts.repository.custom.APPIdCustomRepository;
import com.lkup.accounts.repository.custom.QueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class APPIdCustomRepositoryImpl implements APPIdCustomRepository {

    @Qualifier("tenantMongoTemplate")
    @Autowired
    MongoTemplate mongoTemplate;

    @Qualifier("globalMongoTemplate")
    @Autowired
    MongoTemplate globalMongoTemplate;

    @Override
    public Optional<AppId> validateExisting(QueryCriteria queryCriteria, String name, String appId) {
        Query query = new Query();
        Criteria orCriteria = new Criteria().orOperator( Criteria.where("appId").is(appId));
        query.addCriteria(orCriteria);
        List<AppId> appIds =  globalMongoTemplate.find(query, AppId.class);
        if(!appIds.isEmpty())
           return Optional.ofNullable(appIds.get(0));
        return Optional.empty();
    }

    @Override
    public Optional<AppId> findByName(QueryCriteria queryCriteria, String name) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("name").is(name)
        );
        Query query = new Query(criteria);
        AppId appIds = globalMongoTemplate.findOne(query, AppId.class);
        return Optional.ofNullable(appIds);
    }

    @Override
    public <S extends AppId> S save(S entity) {
       return globalMongoTemplate.save(entity);
    }

    @Override
    public void deleteById(QueryCriteria queryCriteria, String id) {

    }

    @Override
    public Optional<AppId> findById(QueryCriteria queryCriteria, String id) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("id").is(id)
        );
        Query query = new Query(criteria);
        AppId appId = globalMongoTemplate.findOne(query, AppId.class);
        return Optional.ofNullable(appId);
    }

    @Override
    public long countAll(QueryCriteria queryCriteria) {
        Query query = new Query();
        return globalMongoTemplate.count(query, AppId.class);
    }

    @Override
    public long count(QueryCriteria queryCriteria) {
        return countAll(queryCriteria);
    }

    @Override
    public List<AppId> findAllAppIds(QueryCriteria queryCriteria) {
        Query query = new Query();
        return globalMongoTemplate.find(query, AppId.class);
    }

    @Override
    public Optional<List<AppId>> findByIds(QueryCriteria queryCriteria, List<String> ids) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("id").in(ids)
        );
        Query query = new Query(criteria);
        return Optional.of(globalMongoTemplate.find(query, AppId.class));
    }

    @Override
    public void deleteAll(QueryCriteria queryCriteria) {

        Query query = new Query();
        globalMongoTemplate.findAllAndRemove(query, AppId.class);
    }

    @Override
    public List<AppId> findByTenantAndTeamId(QueryCriteria queryCriteria) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("organization.id").is(queryCriteria.getTenantId()),
                Criteria.where("team.id").is(queryCriteria.getTeamId())
        );
        Query query = new Query(criteria);
        return globalMongoTemplate.find(query, AppId.class);
    }
}
