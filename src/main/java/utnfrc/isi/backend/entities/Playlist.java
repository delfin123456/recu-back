package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PLAYLISTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlist_seq")
    @SequenceGenerator(name = "playlist_seq", sequenceName = "SEQ_PLAYLIST_ID", allocationSize = 1)
    @Column(name = "PLAYLIST_ID")
    private Integer playlistId;
    
    @Column(name = "NAME", length = 120)
    private String name;
}