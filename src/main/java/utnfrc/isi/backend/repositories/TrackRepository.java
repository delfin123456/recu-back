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
}