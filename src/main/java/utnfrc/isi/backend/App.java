package utnfrc.isi.backend;

import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.CsvLoaderService;
import utnfrc.isi.backend.services.TrackService;

public class App {
    
    public static void main(String[] args) {
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║        RECUPERATORIO BACKEND - PRE-ENUNCIADO 2025         ║");
        System.out.println("║              Base de Datos de Música (H2)                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        try {
            // ============================================================
            // PASO 1: INICIALIZAR LA BASE DE DATOS
            // ============================================================
            System.out.println("[INICIO] Inicializando base de datos H2...");
            DbContext.initialize();
            
            // ============================================================
            // PASO 2: CARGAR DATOS DESDE EL CSV
            // ============================================================
            // Aquí se especifica la ruta del archivo CSV
            String csvPath = "src/main/resources/files/tracks.csv";
            
            CsvLoaderService csvLoader = new CsvLoaderService(csvPath);
            csvLoader.execute();
            
            // ============================================================
            // PASO 3: EJECUTAR CONSULTAS Y MOSTRAR RESULTADOS
            // ============================================================
            TrackService statsService = new TrackService();
            statsService.execute();
            
            // ============================================================
            // PASO 4: MENSAJE FINAL
            // ============================================================
            System.out.println("╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║  [OK] Proceso completado exitosamente                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
            
        } catch (Exception e) {
            System.err.println("\n[ERROR] Fallo en la ejecución del programa:");
            e.printStackTrace();
            System.exit(1);
            
        } finally {
            // ============================================================
            // PASO 5: CERRAR RECURSOS
            // ============================================================
            DbContext.close();
        }
    }
}