package utnfrc.isi.backend.services;

import jakarta.persistence.EntityManager;
import utnfrc.isi.backend.entities.Album;
import utnfrc.isi.backend.entities.Artist;
import utnfrc.isi.backend.entities.Genre;
import utnfrc.isi.backend.entities.MediaType;
import utnfrc.isi.backend.entities.Track;
import utnfrc.isi.backend.repositories.context.DbContext;
import utnfrc.isi.backend.services.interfaces.IService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.List;

public class CsvLoaderService implements IService {

    private final String csvPath;
    private int tracksInserted = 0;
    private int tracksSkipped = 0;

    public CsvLoaderService(String csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public void execute() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           IMPORTACIÓN DE TRACKS DESDE CSV                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        System.out.println("[INFO] Leyendo archivo: " + csvPath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Saltar encabezado
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                processLine(line);
            }

            System.out.println("\n[✓] Importación completada:");
            System.out.println("  • Tracks insertados: " + tracksInserted);
            System.out.println("  • Tracks omitidos (datos incompletos / error): " + tracksSkipped);
            System.out.println();

        } catch (Exception e) {
            System.err.println("[ERROR] Error al leer el CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Formato CSV:
     * name,albumTitle,artistName,mediaTypeName,genreName,composer,milliseconds,bytes,unitPrice,trackId
     */
    private void processLine(String line) {
        try {
            String[] parts = line.split(",", -1); // -1 para no perder columnas vacías

            if (hasEmptyFields(parts)) {
                tracksSkipped++;
                return;
            }

            String trackName     = parts[0].trim();
            String albumTitle    = parts[1].trim();
            String artistName    = parts[2].trim();
            String mediaTypeName = parts[3].trim();
            String genreName     = parts[4].trim();
            String composer      = parts[5].trim().isEmpty() ? null : parts[5].trim();
            Integer milliseconds = Integer.parseInt(parts[6].trim());
            Integer bytes        = parts[7].trim().isEmpty() ? null : Integer.parseInt(parts[7].trim());
            BigDecimal unitPrice = new BigDecimal(parts[8].trim());
            Integer trackId      = (parts.length > 9 && !parts[9].trim().isEmpty())
                    ? Integer.parseInt(parts[9].trim())
                    : null;

            EntityManager em = DbContext.getEntityManager();
            try {
                em.getTransaction().begin();

                // Buscar o crear dinámicamente entidades relacionadas
                Artist artist       = findOrCreateArtist(em, artistName);
                Album album         = findOrCreateAlbum(em, albumTitle, artist);
                MediaType mediaType = findOrCreateMediaType(em, mediaTypeName);
                Genre genre         = findOrCreateGenre(em, genreName);

                // Crear track
                Track track = new Track();
/*               if (trackId != null) {
                    track.setTrackId(trackId);
                } */
                track.setName(trackName);
                track.setAlbum(album);
                track.setMediaType(mediaType);
                track.setGenre(genre);
                track.setComposer(composer);
                track.setMilliseconds(milliseconds);
                track.setBytes(bytes);
                track.setUnitPrice(unitPrice);

                em.persist(track);

                em.getTransaction().commit();
                tracksInserted++;

            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                tracksSkipped++;
                System.err.println("[WARN] Fila omitida por error: " + e.getMessage());
            } finally {
                em.close();
            }

        } catch (Exception e) {
            tracksSkipped++;
            System.err.println("[WARN] Fila omitida por error de parseo: " + e.getMessage());
        }
    }

    /**
     * Verifica que no falten campos obligatorios.
     */
    private boolean hasEmptyFields(String[] parts) {
        // Necesitamos al menos 9 columnas obligatorias (hasta unitPrice)
        if (parts.length < 9) return true;

        return parts[0].trim().isEmpty() ||  // name
               parts[1].trim().isEmpty() ||  // albumTitle
               parts[2].trim().isEmpty() ||  // artistName
               parts[3].trim().isEmpty() ||  // mediaTypeName
               parts[4].trim().isEmpty() ||  // genreName
               parts[6].trim().isEmpty() ||  // milliseconds
               parts[8].trim().isEmpty();    // unitPrice
    }

    // ================== Helpers "find or create" ==================

    private Artist findOrCreateArtist(EntityManager em, String name) {
        List<Artist> results = em.createQuery(
                        "SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        Artist artist = new Artist();
        artist.setName(name);
        em.persist(artist);
        return artist;
    }

    private Album findOrCreateAlbum(EntityManager em, String title, Artist artist) {
        List<Album> results = em.createQuery(
                        "SELECT a FROM Album a WHERE a.title = :title AND a.artist = :artist", Album.class)
                .setParameter("title", title)
                .setParameter("artist", artist)
                .setMaxResults(1)
                .getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        em.persist(album);
        return album;
    }

    private MediaType findOrCreateMediaType(EntityManager em, String name) {
        List<MediaType> results = em.createQuery(
                        "SELECT m FROM MediaType m WHERE m.name = :name", MediaType.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        MediaType media = new MediaType();
        media.setName(name);
        em.persist(media);
        return media;
    }

    private Genre findOrCreateGenre(EntityManager em, String name) {
        List<Genre> results = em.createQuery(
                        "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        Genre genre = new Genre();
        genre.setName(name);
        em.persist(genre);
        return genre;
    }

    // Getters por si querés mostrar estadísticas en App
    public int getTracksInserted() {
        return tracksInserted;
    }

    public int getTracksSkipped() {
        return tracksSkipped;
    }
}
