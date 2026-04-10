package com.tfg.cultura.api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://root:root@localhost:27018/mydatabase?authSource=admin}")
    private String mongoUri;

    private static final Logger logger = LoggerFactory.getLogger(MongoConfiguration.class);

    @Bean
    @Primary
    public MongoClient mongoClient() {
        logger.info("🔍 MongoDB Configuration - Using URI: {}", mongoUri);
        return MongoClients.create(mongoUri);
    }

}
