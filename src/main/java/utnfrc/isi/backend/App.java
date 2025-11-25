package utnfrc.isi.backend;

import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.AlbumService;
import utnfrc.isi.backend.services.CsvLoaderService;
import utnfrc.isi.backend.services.GenreService;
import utnfrc.isi.backend.services.TrackService;

public class App {
    
    public static void main(String[] args) {
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║        RECUPERATORIO BACKEND - PRE-ENUNCIADO 2025         ║");
        System.out.println("║              Base de Datos de Música (H2)                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        try {
            //1
            System.out.println("[INICIO] Inicializando base de datos H2...");
            DbContext.initialize();
            
            //2:cargar csv
            String csvPath = "src/main/resources/files/data.csv";
            
            CsvLoaderService csvLoader = new CsvLoaderService(csvPath);
            csvLoader.execute();
            
            //3: albumes mas largos
            AlbumService albumService = new AlbumService();
            albumService.execute();
            
            //4: ranking de generos por precio promedio
            GenreService genreService = new GenreService();
            genreService.execute();
  
            System.out.println("╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║  [OK] Todos los informes generados exitosamente          ║");
            System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
            
        } catch (Exception e) {
            System.err.println("\n[ERROR] Fallo en la ejecución del programa:");
            e.printStackTrace();
            System.exit(1);
            
        } finally {
        
            DbContext.close();
        }
    }
}