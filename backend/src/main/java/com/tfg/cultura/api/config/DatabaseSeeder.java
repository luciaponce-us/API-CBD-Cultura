package com.tfg.cultura.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.bson.Document;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Profile("seed")
public class DatabaseSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    public DatabaseSeeder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("🌱 Iniciando Database Seeder...");
        
        try {
            seedDatabase();
            logger.info("✅ Database seeding completado exitosamente");
        } catch (Exception e) {
            logger.error("❌ Error durante el seeding: {}", e.getMessage());
        }
    }

    private void seedDatabase() {
        clearDatabase();
        
        seedUsuarios();
        
        logger.info("💾 Todos los datos se han guardado correctamente");
    }

    private void clearDatabase() {
        logger.info("🗑️  Limpiando base de datos...");
        mongoTemplate.getDb().listCollectionNames().forEach(collectionName -> {
            if (!collectionName.startsWith("system.")) {
                mongoTemplate.dropCollection(collectionName);
                logger.info("   - Colección eliminada: {}", collectionName);
            }
        });
    }

    private void seedUsuarios() {
        logger.info("👥 Creando colección: usuarios");
        List<Document> usuarios = new ArrayList<>();
        
        String now = ZonedDateTime.now().toString();
        
        // TODO: Cambiar por estructura de datos real de usuarios // NOSONAR
        usuarios.add(new Document()
            .append("_id", "user_1")
            .append("nombre", "Admin Cultura")
            .append("email", "admin@cultura.es")
            .append("rol", "administrador")
            .append("activo", true)
            .append("createdAt", now)
        );
        
        mongoTemplate.getCollection("usuarios").insertMany(usuarios);
        logger.info("   ✓ Insertados {} usuarios", usuarios.size());
    }

}

