package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "CUSTOMERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "SEQ_CUSTOMER_ID", allocationSize = 1)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @Column(name = "FIRST_NAME", nullable = false, length = 40)
    private String firstName;
    
    @Column(name = "LAST_NAME", nullable = false, length = 20)
    private String lastName;
    
    @Column(name = "COMPANY", length = 80)
    private String company;
    
    @Column(name = "ADDRESS", length = 70)
    private String address;
    
    @Column(name = "CITY", length = 40)
    private String city;
    
    @Column(name = "STATE", length = 40)
    private String state;
    
    @Column(name = "COUNTRY", length = 40)
    private String country;
    
    @Column(name = "POSTAL_CODE", length = 10)
    private String postalCode;
    
    @Column(name = "PHONE", length = 24)
    private String phone;
    
    @Column(name = "FAX", length = 24)
    private String fax;
    
    @Column(name = "EMAIL", nullable = false, length = 60)
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "SUPPORT_REP_ID")
    private Employee supportRep;
}