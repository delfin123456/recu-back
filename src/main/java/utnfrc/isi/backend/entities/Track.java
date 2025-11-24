package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "TRACKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_seq")
    @SequenceGenerator(name = "track_seq", sequenceName = "SEQ_TRACK_ID", allocationSize = 1)
    @Column(name = "TRACK_ID")
    private Integer trackId;
    
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "ALBUM_ID")
    private Album album;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "MEDIA_TYPE_ID", nullable = false)
    private MediaType mediaType;
    
    @ManyToOne
    @JoinColumn(name = "GENRE_ID")
    private Genre genre;
    
    @Column(name = "COMPOSER", length = 220)
    private String composer;
    
    @Column(name = "MILLISECONDS", nullable = false)
    private Integer milliseconds;
    
    @Column(name = "BYTES")
    private Integer bytes;
    
    @Column(name = "UNIT_PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    // Métodos helper sugeridos en el pre-enunciado
    public double getDurationInMinutes() {
        return milliseconds != null ? milliseconds / 60000.0 : 0.0;
    }
    
    public boolean hasValidPrice() {
        return unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0;
    }
}