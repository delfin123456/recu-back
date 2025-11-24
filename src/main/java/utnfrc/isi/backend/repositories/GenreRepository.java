package utnfrc.isi.backend.repositories;

import utnfrc.isi.backend.entities.Genre;
import utnfrc.isi.backend.repositories.context.DbContext;
import jakarta.persistence.EntityManager;

public class GenreRepository extends Repository<Genre> {
    
    public GenreRepository() {
        super(Genre.class);
    }
    
    // Buscar género por nombre
    public Genre findByName(String name) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}