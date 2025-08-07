package io.github.xico26.spotifum2;

import io.github.xico26.spotifum2.dao.*;
import io.github.xico26.spotifum2.model.SpotifUM;
import io.github.xico26.spotifum2.service.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Metodo de entrada na aplicação. Apenas cria um modelo e passa-o ao controlador.
 */
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("spotifumPU");

        AlbumDAO albumDAO = new AlbumDAOImpl(emf);
        ArtistDAO artistDAO = new ArtistDAOImpl(emf);
        LibraryDAO libraryDAO = new LibraryDAOImpl(emf);
        ListeningRecordDAO lrDAO = new ListeningRecordDAOImpl(emf);
        PlaylistDAO playlistDAO = new PlaylistDAOImpl(emf);
        UserDAO userDAO = new UserDAOImpl(emf);
        MusicDAO musicDAO = new MusicDAOImpl(emf);

        ArtistService artistService = new ArtistService(artistDAO);
        AlbumService albumService = new AlbumService(albumDAO, artistService);
        MusicService musicService = new MusicService(musicDAO, albumService);
        UserService userService = new UserService(userDAO);
        LibraryService libraryService = new LibraryService(libraryDAO);
        ListeningRecordService listeningRecordService = new ListeningRecordService(lrDAO, userService);
        PlaylistService playlistService = new PlaylistService(playlistDAO, libraryService, listeningRecordService, musicService, albumService);
        musicService.setPlaylistService(playlistService);
        musicService.setLibraryService(libraryService);


        Controller controller = new Controller(albumService, artistService, libraryService, listeningRecordService, playlistService, userService, musicService);

        controller.run();
    }
}