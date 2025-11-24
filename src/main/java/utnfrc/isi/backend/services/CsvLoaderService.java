package utnfrc.isi.backend.services;

import utnfrc.isi.backend.entities.*;
import utnfrc.isi.backend.repositories.*;
import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.interfaces.IService;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

public class CsvLoaderService implements IService {
    
    private final String csvPath;
    private final AlbumRepository albumRepo;
    private final MediaTypeRepository mediaTypeRepo;
    private final GenreRepository genreRepo;
    private int tracksInserted = 0;
    private int tracksSkipped = 0;
    
    public CsvLoaderService(String csvPath) {
        this.csvPath = csvPath;
        this.albumRepo = new AlbumRepository();
        this.mediaTypeRepo = new MediaTypeRepository();
        this.genreRepo = new GenreRepository();
    }
    
    @Override
    public void execute() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           IMPORTACIÓN DE TRACKS DESDE CSV                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("[INFO] Leyendo archivo: " + csvPath);
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Saltar la primera línea (encabezado)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Procesar línea
                processLine(line);
            }
            
            System.out.println("\n[✓] Importación completada:");
            System.out.println("  • Tracks insertados: " + tracksInserted);
            System.out.println("  • Tracks omitidos (datos vacíos): " + tracksSkipped);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al leer el CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void processLine(String line) {
        try {
            // Formato CSV: nombre,albumId,mediaTypeId,genreId,composer,milliseconds,bytes,unitPrice,trackId
            String[] parts = line.split(",");
            
            // Validar que no haya campos vacíos (según el pre-enunciado del parcial)
            if (hasEmptyFields(parts)) {
                tracksSkipped++;
                return;
            }
            
            // Extraer datos
            String nombre = parts[0].trim();
            Integer albumId = Integer.parseInt(parts[1].trim());
            Integer mediaTypeId = Integer.parseInt(parts[2].trim());
            Integer genreId = parts[3].trim().isEmpty() ? null : Integer.parseInt(parts[3].trim());
            String composer = parts[4].trim().isEmpty() ? null : parts[4].trim();
            Integer milliseconds = Integer.parseInt(parts[5].trim());
            Integer bytes = parts[6].trim().isEmpty() ? null : Integer.parseInt(parts[6].trim());
            BigDecimal unitPrice = new BigDecimal(parts[7].trim());
            Integer trackId = parts[8].trim().isEmpty() ? null : Integer.parseInt(parts[8].trim());
            
            // Buscar entidades relacionadas
            Album album = albumRepo.findById(albumId);
            MediaType mediaType = mediaTypeRepo.findById(mediaTypeId);
            Genre genre = genreId != null ? genreRepo.findById(genreId) : null;
            
            if (album == null || mediaType == null) {
                tracksSkipped++;
                return;
            }
            
            // Crear y guardar el Track
            EntityManager em = DbContext.getEntityManager();
            try {
                em.getTransaction().begin();
                
                Track track = new Track();
                if (trackId != null) {
                    track.setTrackId(trackId);
                }
                track.setName(nombre);
                track.setAlbum(album);
                track.setMediaType(mediaType);
                track.setGenre(genre);
                track.setComposer(composer);
                track.setMilliseconds(milliseconds);
                track.setBytes(bytes);
                track.setUnitPrice(unitPrice);
                
                em.persist(track);
                em.getTransaction().commit();
                
                tracksInserted++;
                
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                tracksSkipped++;
            } finally {
                em.close();
            }
            
        } catch (Exception e) {
            tracksSkipped++;
        }
    }
    
    private boolean hasEmptyFields(String[] parts) {
        // Según el enunciado del parcial: ignorar filas con campos vacíos
        if (parts.length < 9) return true;
        
        // Verificar campos obligatorios
        return parts[0].trim().isEmpty() ||  // nombre
               parts[1].trim().isEmpty() ||  // albumId
               parts[2].trim().isEmpty() ||  // mediaTypeId
               parts[5].trim().isEmpty() ||  // milliseconds
               parts[7].trim().isEmpty();    // unitPrice
    }
    
    public int getTracksInserted() {
        return tracksInserted;
    }
    
    public int getTracksSkipped() {
        return tracksSkipped;
    }
}