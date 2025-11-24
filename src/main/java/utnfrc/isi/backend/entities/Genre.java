package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "GENRES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    @SequenceGenerator(name = "genre_seq", sequenceName = "SEQ_GENRE_ID", allocationSize = 1)
    @Column(name = "GENRE_ID")
    private Integer genreId;
    
    @Column(name = "NAME", length = 120)
    private String name;
}