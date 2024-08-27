package com.lkup.accounts.config.database;

import com.lkup.accounts.config.database.condition.MultiDbTenantCondition;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Conditional(MultiDbTenantCondition.class)
@EnableMongoRepositories(basePackages = "com.lkup.accounts.repository.tenant", mongoTemplateRef = "tenantMongoTemplate")
public class MultiTenantMongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new MultiTenantMongoDatabaseFactory(mongoClient());

    }

    @Bean(name = "tenantMongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }


}
