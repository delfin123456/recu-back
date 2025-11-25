package utnfrc.isi.backend.services;

import java.util.List;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.interfaces.IService;

public class AlbumService implements IService {

    

    @Override
    public void execute() {
        // Implementación del servicio de álbumes

        showTop10LongestAlbums();
    }

    public void showTop10LongestAlbums() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║      INFORME 2 — TOP 10 ÁLBUMES MÁS LARGOS               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT a.title, ar.name, SUM(t.milliseconds) as totalDuration " +
                         "FROM Track t " +
                         "JOIN t.album a " +
                         "JOIN a.artist ar " +
                         "WHERE t.mediaType.name <> 'Protected MPEG-4 video file' " +
                         "GROUP BY a.albumId, a.title, ar.name " +
                         "ORDER BY totalDuration DESC";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setMaxResults(10)
                    .getResultList();

            if (results.isEmpty()) {
                System.out.println("  No hay álbumes con tracks en la base de datos.");
            } else {
                int position = 1;
                for (Object[] row : results) {
                    String albumTitle = (String) row[0];
                    String artistName = (String) row[1];
                    Long totalMillis = (Long) row[2];

                    // Convertir milisegundos a minutos:segundos (truncar decimales)
                    int totalSeconds = (int) (totalMillis / 1000);
                    int minutes = totalSeconds / 60;
                    int seconds = totalSeconds % 60;

                    System.out.println("  " + position + ". \"" + albumTitle + "\" - " + artistName);
                    System.out.println("     Duración total: " + minutes + ":" + String.format("%02d", seconds));
                    System.out.println();
                    position++;
                }
            }

        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo generar el informe: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}