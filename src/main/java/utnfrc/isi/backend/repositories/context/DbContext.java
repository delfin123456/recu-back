package utnfrc.isi.backend.repositories.context;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DbContext {
    
    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT = "musicPU";
    private static final String DB_URL = "jdbc:h2:mem:database;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    // Inicializar la base de datos ejecutando el DDL
    public static void initialize() {
        try {
            System.out.println("[INFO] Inicializando base de datos H2...");
            
            // Cargar y ejecutar el DDL
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        DbContext.class.getClassLoader()
                            .getResourceAsStream("META-INF/db-schema.sql")
                    )
                )) {
                
                // Leer el archivo DSDL desde resources
                String ddl = reader.lines().collect(Collectors.joining("\n"));
                
                // Ejecutar el DDL
                stmt.execute(ddl);
                System.out.println("[OK] Base de datos inicializada correctamente.");
            }
            
            // Crear EntityManagerFactory
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            System.out.println("[OK] EntityManagerFactory creado.");
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    // Obtener EntityManager
    public static EntityManager getEntityManager() {
        if (emf == null) {
            initialize();
        }
        return emf.createEntityManager();
    }
    
    // Cerrar EntityManagerFactory
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("[INFO] EntityManagerFactory cerrado.");
        }
    }
}