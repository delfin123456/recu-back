package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ALBUMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_seq")
    @SequenceGenerator(name = "album_seq", sequenceName = "SEQ_ALBUM_ID", allocationSize = 1)
    @Column(name = "ALBUM_ID")
    private Integer albumId;
    
    @Column(name = "TITLE", nullable = false, length = 160)
    private String title;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ARTIST_ID", nullable = false)
    private Artist artist;
}