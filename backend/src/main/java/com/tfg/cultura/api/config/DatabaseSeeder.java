package com.tfg.cultura.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
@Profile("seed")
public class DatabaseSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private static final Logger logger = Logger.getLogger(DatabaseSeeder.class.getName());

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
            logger.severe("❌ Error durante el seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seedDatabase() {
        // Eliminar todas las colecciones existentes
        clearDatabase();
        
        // Crear nuevas colecciones con datos
        seedEventos();
        seedUsuarios();
        seedActividades();
        seedCategorias();
        
        logger.info("💾 Todos los datos se han guardado correctamente");
    }

    private void clearDatabase() {
        logger.info("🗑️  Limpiando base de datos...");
        mongoTemplate.getDb().listCollectionNames().forEach(collectionName -> {
            if (!collectionName.startsWith("system.")) {
                mongoTemplate.dropCollection(collectionName);
                logger.info("   - Colección eliminada: " + collectionName);
            }
        });
    }

    private void seedEventos() {
        logger.info("📅 Creando colección: eventos");
        List<Document> eventos = new ArrayList<>();
        
        String now = ZonedDateTime.now().toString();
        
        eventos.add(new Document()
            .append("_id", "evento_1")
            .append("titulo", "Concierto de Jazz")
            .append("descripcion", "No te pierdas este increíble concierto de jazz en la sala principal")
            .append("fecha", "2026-05-15")
            .append("hora", "20:00")
            .append("ubicacion", "ETSII - Sala de Actos")
            .append("capacidad", 200)
            .append("categorias", List.of("musica", "concierto"))
            .append("organizador", "Delegación de Cultura")
            .append("estado", "confirmado")
            .append("createdAt", now)
            .append("updatedAt", now)
        );
        
        eventos.add(new Document()
            .append("_id", "evento_2")
            .append("titulo", "Exposición de Arte Contemporáneo")
            .append("descripcion", "Muestra de obras de estudiantes y artistas locales")
            .append("fecha", "2026-05-20")
            .append("hora", "18:00")
            .append("ubicacion", "ETSII - Galería del Aulario")
            .append("capacidad", 150)
            .append("categorias", List.of("arte", "exposicion"))
            .append("organizador", "Delegación de Cultura")
            .append("estado", "confirmado")
            .append("createdAt", now)
            .append("updatedAt", now)
        );
        
        eventos.add(new Document()
            .append("_id", "evento_3")
            .append("titulo", "Charla sobre Sostenibilidad")
            .append("descripcion", "Conversatorio sobre desarrollo sostenible en la ingeniería")
            .append("fecha", "2026-05-25")
            .append("hora", "17:00")
            .append("ubicacion", "ETSII - Aula Magna")
            .append("capacidad", 300)
            .append("categorias", List.of("conferencia", "sostenibilidad"))
            .append("organizador", "Delegación de Cultura")
            .append("estado", "confirmado")
            .append("createdAt", now)
            .append("updatedAt", now)
        );
        
        mongoTemplate.getCollection("eventos").insertMany(eventos);
        logger.info("   ✓ Insertados " + eventos.size() + " eventos");
    }

    private void seedUsuarios() {
        logger.info("👥 Creando colección: usuarios");
        List<Document> usuarios = new ArrayList<>();
        
        String now = ZonedDateTime.now().toString();
        
        usuarios.add(new Document()
            .append("_id", "user_1")
            .append("nombre", "Admin Cultura")
            .append("email", "admin@cultura.es")
            .append("rol", "administrador")
            .append("activo", true)
            .append("createdAt", now)
        );
        
        usuarios.add(new Document()
            .append("_id", "user_2")
            .append("nombre", "Juan García")
            .append("email", "juan@estudiante.es")
            .append("rol", "usuario")
            .append("activo", true)
            .append("createdAt", now)
        );
        
        usuarios.add(new Document()
            .append("_id", "user_3")
            .append("nombre", "María López")
            .append("email", "maria@estudiante.es")
            .append("rol", "usuario")
            .append("activo", true)
            .append("createdAt", now)
        );
        
        mongoTemplate.getCollection("usuarios").insertMany(usuarios);
        logger.info("   ✓ Insertados " + usuarios.size() + " usuarios");
    }

    private void seedActividades() {
        logger.info("🎭 Creando colección: actividades");
        List<Document> actividades = new ArrayList<>();
        
        String now = ZonedDateTime.now().toString();
        
        actividades.add(new Document()
            .append("_id", "act_1")
            .append("nombre", "Taller de Fotografía")
            .append("descripcion", "Aprende técnicas básicas de fotografía digital")
            .append("duracion", "2 horas")
            .append("tipo", "taller")
            .append("nivel", "principiante")
            .append("participantes_max", 30)
            .append("estado", "activo")
            .append("createdAt", now)
        );
        
        actividades.add(new Document()
            .append("_id", "act_2")
            .append("nombre", "Club de Lectura")
            .append("descripcion", "Discussión mensual sobre libros de interés")
            .append("duracion", "1.5 horas")
            .append("tipo", "club")
            .append("nivel", "general")
            .append("participantes_max", 20)
            .append("estado", "activo")
            .append("createdAt", now)
        );
        
        actividades.add(new Document()
            .append("_id", "act_3")
            .append("nombre", "Masterclass de Cine")
            .append("descripcion", "Análisis crítico del cine contemporáneo con expertos")
            .append("duracion", "3 horas")
            .append("tipo", "masterclass")
            .append("nivel", "intermedio")
            .append("participantes_max", 50)
            .append("estado", "activo")
            .append("createdAt", now)
        );
        
        mongoTemplate.getCollection("actividades").insertMany(actividades);
        logger.info("   ✓ Insertadas " + actividades.size() + " actividades");
    }

    private void seedCategorias() {
        logger.info("📂 Creando colección: categorias");
        List<Document> categorias = new ArrayList<>();
        String now = ZonedDateTime.now().toString();
        
        String[] categoriasList = {
            "musica", "arte", "conferencia", "taller", "deporte", 
            "exposicion", "cine", "teatro", "sostenibilidad", "tecnologia"
        };
        
        for (int i = 0; i < categoriasList.length; i++) {
            categorias.add(new Document()
                .append("_id", "cat_" + (i + 1))
                .append("nombre", categoriasList[i])
                .append("descripcion", "Categoría de " + categoriasList[i])
                .append("icono", "icon-" + categoriasList[i])
                .append("color", getColorForCategory(categoriasList[i]))
                .append("activa", true)
                .append("orden", i + 1)
                .append("createdAt", now)
            );
        }
        
        mongoTemplate.getCollection("categorias").insertMany(categorias);
        logger.info("   ✓ Insertadas " + categorias.size() + " categorías");
    }

    private String getColorForCategory(String categoria) {
        return switch (categoria) {
            case "musica" -> "#FF6B6B";
            case "arte" -> "#4ECDC4";
            case "conferencia" -> "#45B7D1";
            case "taller" -> "#FFA07A";
            case "deporte" -> "#98D8C8";
            case "exposicion" -> "#F7DC6F";
            case "cine" -> "#BB8FCE";
            case "teatro" -> "#85C1E2";
            case "sostenibilidad" -> "#52BE80";
            case "tecnologia" -> "#5DADE2";
            default -> "#95A5A6";
        };
    }
}
