package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.InvoiceItem;
import utnfrc.isi.backend.repositories.context.DbContext;

public class InvoiceItemRepository extends Repository<InvoiceItem> {

    public InvoiceItemRepository() {
        super(InvoiceItem.class);
    }

    // Obtener líneas de factura por factura
    public List<InvoiceItem> findByInvoiceId(Integer invoiceId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT ii FROM InvoiceItem ii WHERE ii.invoice.invoiceId = :invoiceId", InvoiceItem.class)
                    .setParameter("invoiceId", invoiceId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}