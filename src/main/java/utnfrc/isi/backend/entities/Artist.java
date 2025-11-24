package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ARTISTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_seq")
    @SequenceGenerator(name = "artist_seq", sequenceName = "SEQ_ARTIST_ID", allocationSize = 1)
    @Column(name = "ARTIST_ID")
    private Integer artistId;
    
    @Column(name = "NAME", length = 120)
    private String name;
}