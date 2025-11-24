package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.repositories.context.DbContext;
import java.util.List;

public abstract class Repository<T> {
    
    protected final Class<T> entityClass;
    
    public Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    // Guardar entidad
    public void save(T entity) {
        EntityManager em = DbContext.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    // Buscar por ID
    public T findById(Object id) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    // Obtener todos
    public List<T> findAll() {
        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } finally {
            em.close();
        }
    }
    
    // Contar registros
    public long count() {
        EntityManager em = DbContext.getEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}