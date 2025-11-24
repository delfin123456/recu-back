package utnfrc.isi.backend.services;

import utnfrc.isi.backend.repositories.TrackRepository;
import utnfrc.isi.backend.services.interfaces.IService;
import java.util.List;

public class TrackService implements IService {
    
    private final TrackRepository trackRepo;
    
    public TrackService() {
        this.trackRepo = new TrackRepository();
    }
    
    @Override
    public void execute() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              ESTADÍSTICAS DE TRACKS                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        // Consulta 1: Contar tracks por género
        countTracksByGenre("Pop");
        
        // Consulta 2: Top 3 álbumes con más tracks
        showTopAlbumsWithMostTracks();

        showTracksByArtist("Michael Jackson");
    }
    
    /**
     * Cuenta y muestra la cantidad de tracks de un género específico
     */
    public void countTracksByGenre(String genreName) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  CONSULTA 1: Tracks por Género");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        try {
            long count = trackRepo.countByGenreName(genreName);
            System.out.println("  Género: " + genreName);
            System.out.println("  Cantidad de tracks: " + count);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo contar tracks del género '" + genreName + "': " + e.getMessage());
        }
    }
    
    /**
     * Muestra los top 3 álbumes con más tracks
     */
    public void showTopAlbumsWithMostTracks() {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  CONSULTA 2: Top 3 Álbumes con Más Tracks");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        try {
            List<Object[]> topAlbums = trackRepo.getTopAlbumsWithMostTracks(3);
            
            if (topAlbums.isEmpty()) {
                System.out.println("  No hay álbumes con tracks en la base de datos.");
            } else {
                int position = 1;
                for (Object[] result : topAlbums) {
                    String albumTitle = (String) result[0];
                    String artistName = (String) result[1];
                    Long trackCount = (Long) result[2];
                    
                    System.out.println("  " + position + ". \"" + albumTitle + "\" - " + artistName);
                    System.out.println("     Cantidad de tracks: " + trackCount);
                    System.out.println();
                    position++;
                }
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo obtener el ranking de álbumes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showTracksByArtist(String artistName) {
    System.out.println("═══════════════════════════════════════════════════════════");
    System.out.println("  CONSULTA 3: Tracks de un Artista");
    System.out.println("═══════════════════════════════════════════════════════════");

    try {
        var tracks = trackRepo.findByArtistName(artistName);

        if (tracks.isEmpty()) {
            System.out.println("  No hay tracks para el artista: " + artistName);
            System.out.println();
            return;
        }

        System.out.println("  Artista: " + artistName);
        System.out.println("  Cantidad de tracks: " + tracks.size());
        System.out.println();

        for (var t : tracks) {
            System.out.println("  - \"" + t.getName() + "\" (" +
                    t.getAlbum().getTitle() + ", " +
                    t.getGenre().getName() + ")");
        }
        System.out.println();

    } catch (Exception e) {
        System.err.println("[ERROR] No se pudieron obtener tracks del artista '" +
                artistName + "': " + e.getMessage());
    }
}

}