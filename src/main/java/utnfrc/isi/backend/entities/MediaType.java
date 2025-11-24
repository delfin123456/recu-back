package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "MEDIA_TYPES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mediatype_seq")
    @SequenceGenerator(name = "mediatype_seq", sequenceName = "SEQ_MEDIA_TYPE_ID", allocationSize = 1)
    @Column(name = "MEDIA_TYPE_ID")
    private Integer mediaTypeId;
    
    @Column(name = "NAME", length = 120)
    private String name;
}
