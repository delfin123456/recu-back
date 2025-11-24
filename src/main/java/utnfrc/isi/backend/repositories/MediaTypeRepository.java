package utnfrc.isi.backend.repositories;

import utnfrc.isi.backend.entities.MediaType;
import utnfrc.isi.backend.repositories.context.DbContext;
import jakarta.persistence.EntityManager;

public class MediaTypeRepository extends Repository<MediaType> {
    
    public MediaTypeRepository() {
        super(MediaType.class);
    }
    
    // Buscar MediaType por nombre
    public MediaType findByName(String name) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM MediaType m WHERE m.name = :name", MediaType.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}