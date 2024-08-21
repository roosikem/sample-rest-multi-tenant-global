package com.lkup.accounts.config.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.lkup.accounts.repository.global", mongoTemplateRef = "globalMongoTemplate")
public class GlobalMongoConfig {

    @Value("${spring.data.mongodb.global.uri}")
    private String globalMongoClientURI;

    @Value("${spring.data.mongodb.global.database}")
    private String globalMongoDb;

    @Bean
    @Primary
    public MongoClient globalMongoClient() {
        return MongoClients.create(globalMongoClientURI);
    }

    @Bean(name = "globalMongoTemplate")
    @Primary
    public MongoTemplate globalMongoTemplate() {
        MongoClient mongoClient = globalMongoClient();
        return new MongoTemplate(mongoClient, globalMongoDb);
    }
}
