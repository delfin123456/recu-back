package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.Invoice;
import utnfrc.isi.backend.repositories.context.DbContext;

public class InvoiceRepository extends Repository<Invoice> {

    public InvoiceRepository() {
        super(Invoice.class);
    }

    // Obtener facturas por cliente
    public List<Invoice> findByCustomerId(Integer customerId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId", Invoice.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}