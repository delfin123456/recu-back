package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.entities.Playlist;
import utnfrc.isi.backend.repositories.context.DbContext;

public class PlaylistRepository extends Repository<Playlist> {

    public PlaylistRepository() {
        super(Playlist.class);
    }

    // Buscar playlist por nombre
    public Playlist findByName(String name) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Playlist p WHERE p.name = :name", Playlist.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}