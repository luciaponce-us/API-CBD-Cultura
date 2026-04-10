package com.tfg.cultura.api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://root:root@localhost:27018/mydatabase?authSource=admin}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClient mongoClient() {
        System.out.println("🔍 MongoDB Configuration - Using URI: " + mongoUri);
        return MongoClients.create(mongoUri);
    }

}
