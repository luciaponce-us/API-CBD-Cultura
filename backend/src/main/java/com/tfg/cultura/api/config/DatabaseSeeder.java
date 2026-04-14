package com.tfg.cultura.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.enumerators.Role;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DatabaseSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger("appLogger");

    public DatabaseSeeder(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("🌱 Iniciando Database Seeder...");
        logger.info(" - Database: {}", mongoTemplate.getDb().getName());
        logger.info(" - MongoTemplate instance: {}", mongoTemplate);
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
        logger.info("👥 Creando colección: users");

        String password = "cultura123"; //NOSONAR

        User coordinador = User.builder()
            .username("coordinador")
            .password(passwordEncoder.encode(password)) //NOSONAR
            .name("Álvaro")
            .surname("Coordinador")
            .dni("12345678A")
            .phone("+34600123456")
            .email("coordinador@cultura.es")
            .active(true)
            .role(Role.COORDINADOR)
            .createdAt(LocalDateTime.now())
            .build();
        
        User secretario = User.builder()
            .username("secretario")
            .password(passwordEncoder.encode(password)) //NOSONAR
            .name("Aurora")
            .surname("Secretaria")
            .dni("87654321B")
            .phone("+34600123457")
            .email("secretario@cultura.es")
            .active(true)
            .role(Role.SECRETARIO)
            .createdAt(LocalDateTime.now())
            .build();
        
        User encargado = User.builder()
            .username("encargado")
            .password(passwordEncoder.encode(password)) //NOSONAR
            .name("Luis")
            .surname("Encargado")
            .dni("11223344C")
            .phone("+34600123458")
            .email("encargado@cultura.es")
            .active(true)
            .role(Role.ENCARGADO)
            .createdAt(LocalDateTime.now())
            .build();
        
        User colaborador = User.builder()
            .username("colaborador")
            .password(passwordEncoder.encode(password)) //NOSONAR
            .name("Atenea")
            .surname("Colaboradora")
            .dni("44332211D")
            .phone("+34600123459")
            .email("colaborador@cultura.es")
            .active(true)
            .role(Role.COLABORADOR)
            .createdAt(LocalDateTime.now())
            .build();
        
        User socio = User.builder()
            .username("socio")
            .password(passwordEncoder.encode(password)) //NOSONAR
            .name("Lucía")
            .surname("Socia")
            .dni("55667788E")
            .phone("+34600123460")
            .email("socio@cultura.es")
            .active(true)
            .role(Role.SOCIO)
            .createdAt(LocalDateTime.now())
            .build();
        
        List<User> usuarios = List.of(
            coordinador,
            secretario,
            encargado,
            colaborador,
            socio
        );

        mongoTemplate.insertAll(usuarios);
        logger.info("✅👥 Insertados {} usuarios", usuarios.size());
    }

}
