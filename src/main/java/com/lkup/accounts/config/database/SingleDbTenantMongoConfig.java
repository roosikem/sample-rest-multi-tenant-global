package com.lkup.accounts.config.database;

import com.lkup.accounts.config.database.condition.SingleDbTenantCondition;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Conditional(SingleDbTenantCondition.class)
@EnableMongoRepositories(basePackages = "com.lkup.accounts.repository.tenant", mongoTemplateRef = "tenantMongoTemplate")
public class SingleDbTenantMongoConfig {

    @Value("${spring.data.mongodb.singleDbMultiTenant.uri}")
    private String singleDbMultiTenantUri;

    @Value("${spring.data.mongodb.singleDbMultiTenant.database}")
    private String singleDbMultiTenantDatabase;

    @Bean
    @Primary
    public MongoClient tenantMongoClient() {
        return MongoClients.create(singleDbMultiTenantUri);
    }

    @Bean(name = "tenantMongoTemplate")
    @Primary
    public MongoTemplate globalMongoTemplate() {
        MongoClient mongoClient = tenantMongoClient();
        return new MongoTemplate(mongoClient, singleDbMultiTenantDatabase);
    }
}
