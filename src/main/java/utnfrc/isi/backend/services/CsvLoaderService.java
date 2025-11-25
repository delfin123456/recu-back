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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvLoaderService implements IService {

    private final String csvPath;
    private int artistsInserted = 0;
    private int albumsInserted = 0;
    private int genresInserted = 0;
    private int mediaTypesInserted = 0;
    private int tracksInserted = 0;
    private int tracksSkipped = 0;

    private final Map<String, Artist> artistCache = new HashMap<>();
    private final Map<String, Album> albumCache = new HashMap<>();
    private final Map<String, Genre> genreCache = new HashMap<>();
    private final Map<String, MediaType> mediaTypeCache = new HashMap<>();


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

            showImportResults();

        } catch (Exception e) {
            System.err.println("[ERROR] Error al leer el CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Formato CSV:
     * trackName;composer;milliseconds;bytes;unitPrice;albumTitle;artistName;genreName;mediaTypeName
     */
    private void processLine(String line) {
        try {
            String[] parts = line.split(";", -1); // -1 para no perder columnas vacías

            if (hasEmptyFields(parts)) {
                tracksSkipped++;
                return;
            }

            String trackName     = parts[0].trim();
            String composer      = parts[1].trim().isEmpty() ? null : parts[1].trim(); // composer SÍ puede ser nulo
            Integer milliseconds = Integer.parseInt(parts[2].trim());
            Integer bytes        = parts[3].trim().isEmpty() ? null : Integer.parseInt(parts[3].trim());
            BigDecimal unitPrice = new BigDecimal(parts[4].trim());
            String albumTitle    = parts[5].trim();
            String artistName    = parts[6].trim();
            String genreName     = parts[7].trim();
            String mediaTypeName = parts[8].trim();

            EntityManager em = DbContext.getEntityManager();
            try {
                em.getTransaction().begin();

                // Buscar o crear dinámicamente entidades relacionadas
                Artist artist       = findOrCreateArtist(em, artistName);
                Album album         = findOrCreateAlbum(em, albumTitle, artist);
                Genre genre         = findOrCreateGenre(em, genreName);
                MediaType mediaType = findOrCreateMediaType(em, mediaTypeName);
                
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
               parts[1].trim().isEmpty() ||  // milliseconds
               parts[2].trim().isEmpty() ||  // unitPrice
               parts[3].trim().isEmpty() ||  // albumTtile
               parts[4].trim().isEmpty() ||  // artistName
               parts[6].trim().isEmpty() ||  // genreName
               parts[8].trim().isEmpty();    // mediaTypeName
    }

    // ================== Helpers "find or create" ==================

    private Artist findOrCreateArtist(EntityManager em, String name) {
        // Verificar caché
        if (artistCache.containsKey(name)) {
            return artistCache.get(name);
        }

        // Buscar en BD
        List<Artist> results = em.createQuery(
                        "SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        Artist artist;
        if (!results.isEmpty()) {
            artist = results.get(0);
        } else {
            artist = new Artist();
            artist.setName(name);
            em.persist(artist);
            artistsInserted++;
        }

        artistCache.put(name, artist);
        return artist;
    }

    private Album findOrCreateAlbum(EntityManager em, String title, Artist artist) {
        String key = title + "|" + artist.getName();

        if (albumCache.containsKey(key)) {
            return albumCache.get(key);
        }

        List<Album> results = em.createQuery(
                        "SELECT a FROM Album a WHERE a.title = :title AND a.artist = :artist", Album.class)
                .setParameter("title", title)
                .setParameter("artist", artist)
                .setMaxResults(1)
                .getResultList();

        Album album;
        if (!results.isEmpty()) {
            album = results.get(0);
        } else {
            album = new Album();
            album.setTitle(title);  
            album.setArtist(artist);
            em.persist(album);
            albumsInserted++;
        }

        albumCache.put(key, album);
        return album;
    }

    private Genre findOrCreateGenre(EntityManager em, String name) {
        if (genreCache.containsKey(name)) {
            return genreCache.get(name);
        }

        List<Genre> results = em.createQuery(
                        "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        Genre genre;
        if (!results.isEmpty()) {
            genre = results.get(0);
        } else {
            genre = new Genre();
            genre.setName(name);
            em.persist(genre);
            genresInserted++;
        }

        genreCache.put(name, genre);
        return genre;
    }

    private MediaType findOrCreateMediaType(EntityManager em, String name) {
        if (mediaTypeCache.containsKey(name)) {
            return mediaTypeCache.get(name);
        }

        List<MediaType> results = em.createQuery(
                        "SELECT m FROM MediaType m WHERE m.name = :name", MediaType.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        MediaType media;
        if (!results.isEmpty()) {
            media = results.get(0);
        } else {
            media = new MediaType();
            media.setName(name);
            em.persist(media);
            mediaTypesInserted++;
        }

        mediaTypeCache.put(name, media);
        return media;
    }

    /**
     * INFORME 1 - Resultado de la importación
     */
    private void showImportResults() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║          INFORME 1 — RESULTADO DE LA IMPORTACIÓN          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        System.out.println("  • Total de artistas insertados:    " + artistsInserted);
        System.out.println("  • Total de álbumes insertados:     " + albumsInserted);
        System.out.println("  • Total de géneros insertados:     " + genresInserted);
        System.out.println("  • Total de media types insertados: " + mediaTypesInserted);
        System.out.println("  • Total de tracks insertados:      " + tracksInserted);
        System.out.println();
    }

    // Getters para acceso externo (opcional)
    public int getTracksInserted() {
        return tracksInserted;
    }

    public int getTracksSkipped() {
        return tracksSkipped;
    }
}