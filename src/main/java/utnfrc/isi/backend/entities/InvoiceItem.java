package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "INVOICE_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoiceline_seq")
    @SequenceGenerator(name = "invoiceline_seq", sequenceName = "SEQ_INVOICE_LINE_ID", allocationSize = 1)
    @Column(name = "INVOICE_LINE_ID")
    private Integer invoiceLineId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "INVOICE_ID", nullable = false)
    private Invoice invoice;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "TRACK_ID", nullable = false)
    private Track track;
    
    @Column(name = "UNIT_PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
}