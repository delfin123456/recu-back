package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "INVOICES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    @SequenceGenerator(name = "invoice_seq", sequenceName = "SEQ_INVOICE_ID", allocationSize = 1)
    @Column(name = "INVOICE_ID")
    private Integer invoiceId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
    
    @Column(name = "INVOICE_DATE", nullable = false)
    private LocalDate invoiceDate;
    
    @Column(name = "BILLING_ADDRESS", length = 70)
    private String billingAddress;
    
    @Column(name = "BILLING_CITY", length = 40)
    private String billingCity;
    
    @Column(name = "BILLING_STATE", length = 40)
    private String billingState;
    
    @Column(name = "BILLING_COUNTRY", length = 40)
    private String billingCountry;
    
    @Column(name = "BILLING_POSTAL_CODE", length = 10)
    private String billingPostalCode;
    
    @Column(name = "TOTAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    // Método helper sugerido en el pre-enunciado
    public boolean hasValidTotal() {
        return total != null && total.compareTo(BigDecimal.ZERO) > 0;
    }
}