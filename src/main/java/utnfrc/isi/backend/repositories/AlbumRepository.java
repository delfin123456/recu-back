package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.entities.Album;
import utnfrc.isi.backend.repositories.context.DbContext;

public class AlbumRepository extends Repository<Album> {

    public AlbumRepository() {
        super(Album.class);
    }

    // Buscar álbum por título
    public Album findByTitle(String title) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Album a WHERE a.title = :title", Album.class)
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    
}