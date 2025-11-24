package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.PlaylistTrack;
import utnfrc.isi.backend.repositories.context.DbContext;

public class PlaylistTrackRepository extends Repository<PlaylistTrack> {

    public PlaylistTrackRepository() {
        super(PlaylistTrack.class);
    }

    // Obtener relaciones por playlist
    public List<PlaylistTrack> findByPlaylistId(Integer playlistId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT pt FROM PlaylistTrack pt WHERE pt.playlist.playlistId = :playlistId", PlaylistTrack.class)
                    .setParameter("playlistId", playlistId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Obtener relación por playlist y track
    public PlaylistTrack findByPlaylistAndTrack(Integer playlistId, Integer trackId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT pt FROM PlaylistTrack pt WHERE pt.playlist.playlistId = :playlistId AND pt.track.trackId = :trackId",
                            PlaylistTrack.class)
                    .setParameter("playlistId", playlistId)
                    .setParameter("trackId", trackId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}