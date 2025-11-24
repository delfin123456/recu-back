package utnfrc.isi.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PLAYLIST_TRACK")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistTrack {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlisttrack_seq")
    @SequenceGenerator(name = "playlisttrack_seq", sequenceName = "SEQ_PLAYLIST_TRACK_ID", allocationSize = 1)
    @Column(name = "PLAYLIST_TRACK_ID")
    private Integer playlistTrackId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYLIST_ID", nullable = false)
    private Playlist playlist;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "TRACK_ID", nullable = false)
    private Track track;
}