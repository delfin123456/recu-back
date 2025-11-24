package utnfrc.isi.backend.repositories;

import utnfrc.isi.backend.entities.Artist;
import utnfrc.isi.backend.repositories.context.DbContext;
import jakarta.persistence.EntityManager;

public class ArtistRepository extends Repository<Artist> {
    
    public ArtistRepository() {
        super(Artist.class);
    }
    
    // Buscar artista por nombre (ejemplo)
    public Artist findByName(String name) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}