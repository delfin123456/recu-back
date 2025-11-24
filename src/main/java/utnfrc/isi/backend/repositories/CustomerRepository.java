package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.Customer;
import utnfrc.isi.backend.repositories.context.DbContext;

public class CustomerRepository extends Repository<Customer> {

    public CustomerRepository() {
        super(Customer.class);
    }

    // Buscar cliente por email
    public Customer findByEmail(String email) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Obtener clientes por país
    public List<Customer> findByCountry(String country) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.country = :country", Customer.class)
                    .setParameter("country", country)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
