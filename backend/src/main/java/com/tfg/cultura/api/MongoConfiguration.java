package com.tfg.cultura.api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration {

    private static final Logger log = LoggerFactory.getLogger("appLogger");

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClient mongoClient() {
        log.info("📦 Configurando MongoClient con URI: {}", mongoUri);
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "cultura_db");
    }

}
