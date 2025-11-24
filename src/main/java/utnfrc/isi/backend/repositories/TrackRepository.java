package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.Track;
import utnfrc.isi.backend.repositories.context.DbContext;

public class TrackRepository extends Repository<Track> {

    public TrackRepository() {
        super(Track.class);
    }

    // Buscar pista por nombre
    public Track findByName(String name) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Track t WHERE t.name = :name", Track.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Obtener pistas por género
    public List<Track> findByGenreId(Integer genreId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Track t WHERE t.genre.genreId = :genreId", Track.class)
                    .setParameter("genreId", genreId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Contar tracks por género específico
    public long countByGenreName(String genreName) {
        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT COUNT(t) FROM Track t WHERE t.genre.name = :genreName";
            return em.createQuery(jpql, Long.class)
                    .setParameter("genreName", genreName)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Top 3 álbumes con más tracks
    public List<Object[]> getTopAlbumsWithMostTracks(int limit) {
        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT t.album.title, t.album.artist.name, COUNT(t) as trackCount " +
                         "FROM Track t " +
                         "WHERE t.album IS NOT NULL " +
                         "GROUP BY t.album.albumId, t.album.title, t.album.artist.name " +
                         "ORDER BY trackCount DESC";
            
            return em.createQuery(jpql, Object[].class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}