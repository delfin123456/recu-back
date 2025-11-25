package utnfrc.isi.backend.services;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.interfaces.IService;
import java.util.List;

public class GenreService implements IService {

    @Override
    public void execute() {
        showTop5GenresByAveragePrice();
    }

    /**
     * INFORME 3 — Ranking de Precio Promedio por Género
     * 
     * Calcula el precio promedio de los tracks por género.
     * Muestra los 5 géneros con precio promedio más alto (de mayor a menor).
     * Excluye tracks de video (nombre del MediaType != "Protected MPEG-4 video file").
     * Solo incluye géneros con al menos 1 track.
     */
    public void showTop5GenresByAveragePrice() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║   INFORME 3 — RANKING PRECIO PROMEDIO POR GÉNERO         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT g.name, AVG(t.unitPrice), COUNT(t) " +
                         "FROM Track t " +
                         "JOIN t.genre g " +
                         "WHERE t.mediaType.name <> 'Protected MPEG-4 video file' " +
                         "GROUP BY g.genreId, g.name " +
                         "HAVING COUNT(t) >= 1 " +
                         "ORDER BY AVG(t.unitPrice) DESC";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setMaxResults(5)
                    .getResultList();

            if (results.isEmpty()) {
                System.out.println("  No hay géneros con tracks en la base de datos.");
            } else {
                System.out.println("  Formato: GÉNERO : PRECIO PROMEDIO : CANTIDAD DE TRACKS\n");
                
                for (Object[] row : results) {
                    String genreName = (String) row[0];
                    Double avgPrice = (Double) row[1];
                    Long trackCount = (Long) row[2];

                    System.out.println("  " + genreName + " : $" + 
                                      String.format("%.2f", avgPrice) + " : " + 
                                      trackCount + " tracks");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo generar el informe: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}