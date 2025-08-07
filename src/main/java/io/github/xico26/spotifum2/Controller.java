package io.github.xico26.spotifum2;

import io.github.xico26.spotifum2.exceptions.*;
import io.github.xico26.spotifum2.model.entity.*;
import io.github.xico26.spotifum2.model.entity.music.ExplicitMusic;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;
import io.github.xico26.spotifum2.model.entity.playlist.RandomPlaylist;
import io.github.xico26.spotifum2.model.entity.playlist.CustomPlaylist;
import io.github.xico26.spotifum2.service.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
/**
 * Controlador da aplicação. Inclui lógica para gerir inputs, impressão de menus, entre outros.
 */
public class Controller {
    private static final Scanner scanner = new Scanner(System.in);
    private boolean loggedIn = false;
    private User currentUser;
    private boolean isAdmin = false;

    // Services
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final LibraryService libraryService;
    private final ListeningRecordService listeningRecordService;
    private final PlaylistService playlistService;
    private final UserService userService;
    private final MusicService musicService;

    public Controller(AlbumService albumService, ArtistService artistService, LibraryService libraryService, ListeningRecordService listeningRecordService, PlaylistService playlistService, UserService userService, MusicService musicService) {
        this.albumService = albumService;
        this.artistService = artistService;
        this.userService = userService;
        this.playlistService = playlistService;
        this.listeningRecordService = listeningRecordService;
        this.libraryService = libraryService;
        this.musicService = musicService;
    }

    /**
     * Entry point
     */
    public void run() {
        mainMenu();
    }

    /**
     * Main menu
     */
    private void mainMenu () {
        Menu mainMenu = new Menu("menu principal", new String[] {
                "Pesquisar...",
                "Ouvir Playlist aleatória",
                "Criar Playlist",
                "User",
                "Estatísticas",
                "Administração",
                "Logout",
                "Login",
                "Criar Conta"
        });
        // Pré Condições
        mainMenu.setPreCondition(2, () -> loggedIn);
        mainMenu.setPreCondition(3, () -> loggedIn);
        mainMenu.setPreCondition(4, () -> loggedIn);
        mainMenu.setPreCondition(5, () -> loggedIn);
        mainMenu.setPreCondition(6, () -> (loggedIn && isAdmin));
        mainMenu.setPreCondition(7, () -> loggedIn);
        mainMenu.setPreCondition(8, () -> !loggedIn);
        mainMenu.setPreCondition(9, () -> !loggedIn);

        // Handlers
        mainMenu.setHandler(1, () -> searchMenu());
        mainMenu.setHandler(2, () -> ouvirPlaylistAleatoria());
        mainMenu.setHandler(3, () -> menuCriarPlaylist());
        mainMenu.setHandler(4, () -> userMenu());
        mainMenu.setHandler(5, () -> statsMenu());
        mainMenu.setHandler(6, () -> adminMenu());
        mainMenu.setHandler(7, () -> logout());
        mainMenu.setHandler(8, () -> login());
        mainMenu.setHandler(9, () -> signup());

        mainMenu.run();
    }

    /**
     * Login UI
     */
    public void login() {
        User res = null;
        int tries = 0;
        do {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            try {
                res = this.userService.login(username, password);
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
            tries++;
            if (tries == 3) {
                System.out.println("Too many tries!");
                return;
            }
        } while (res == null);
        this.loggedIn = true;
        this.currentUser = res;
        this.isAdmin = res.isAdmin();
        System.out.println("Login successful! Welcome, " + currentUser.getName() + "!");
        mainMenu();
    }

    /**
     * Logout frontend logic
     */
    public void logout() {
        this.loggedIn = false;
        this.isAdmin = false;
        this.currentUser = null;
        System.out.println("Adeus!");
        mainMenu();
    }

    /**
     * Account creation UI.
     */
    public void signup() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.println("Birth Date");
        int day = 0;
        int month = 0;
        int year = 0;
        LocalDate birthDate = LocalDate.of(2005,1,1);
        try {
            System.out.print("Day: ");
            day = scanner.nextInt();
            System.out.print("Month: ");
            month = scanner.nextInt();
            System.out.print("Year: ");
            year = scanner.nextInt();
            birthDate = LocalDate.of(year, month, day);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
            return;
        } catch (DateTimeException e) {
            System.out.println("Invalid date!");
            return;
        }
        scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();


        try {
            this.userService.createUser(username, password, name, address, email, birthDate);
        } catch (InvalidParamsException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Account created successfully! You can now login.");
    }

    /**
     * Search menu
     */
    public void searchMenu() {
        Menu searchMenu = new Menu("search...", new String[]{
            "Search for music",
            "Search for album",
            "Search for playlist",
            "Search for artist",
        });
        searchMenu.setHandler(1, () -> categorySearchMenu("music"));
        searchMenu.setHandler(2, () -> categorySearchMenu("album"));
        searchMenu.setHandler(3, () -> categorySearchMenu("playlist"));
        searchMenu.setHandler(4, () -> categorySearchMenu("artist"));

        searchMenu.run();
    }

    /**
     * User settings/overview menu.
     */
    public void userMenu() {
        Menu userMenu = new Menu("my profile", new String[]{
            "Explore library",
            "Settings",
            "Logout",
        });
        userMenu.setHandler(1, () -> exploreLibrary());
        userMenu.setHandler(2, () -> userSettingsMenu());
        userMenu.setHandler(3, () -> logout());

        userMenu.run();
    }

    /**
     * Menu que contém as definições de um utilizador, como atualizar o plano, mostrar / esconder músicas e apagar a conta.
     */
    private void userSettingsMenu() {
        Menu userSettingsMenu = new Menu("my settings", new String[] {
                "Update plan",
                "Clear listening history",
                "My informations",
                "Toggle displaying of explicit music. Currently they are " + (currentUser.wantsExplicit() ? "shown" : "hidden"),
                "Become administrator",
                "Delete account"
        });

        userSettingsMenu.setPreCondition(6, () -> !isAdmin);

        userSettingsMenu.setHandler(1, () -> updatePlanMenu());
        userSettingsMenu.setHandler(2, () -> this.listeningRecordService.clearHistory(currentUser));
        userSettingsMenu.setHandler(3, () -> userInfoMenu());
        userSettingsMenu.setHandler(4, () -> this.currentUser.setWantsExplicit(!currentUser.wantsExplicit()));
        userSettingsMenu.setHandler(5, () -> {
            this.currentUser.setIsAdmin(true);
            this.isAdmin = true;
        });
        userSettingsMenu.setHandler(6, () -> deleteAccountMenu());

        userSettingsMenu.run();
    }

    /**
     * UI for choosing a new plan
     */
    private void updatePlanMenu() {
        Menu planMenu = new Menu("update plan", new String[] {
                "Free",
                "Plus",
                "Premium",
        });

        planMenu.setPreCondition(1, () -> !(currentUser.getSubscriptionPlan().equals("FREE")));
        planMenu.setPreCondition(2, () -> !(currentUser.getSubscriptionPlan().equals("PLUS")));
        planMenu.setPreCondition(3, () -> !(currentUser.getSubscriptionPlan().equals("PREMIUM")));

        planMenu.setHandler(1, () -> {
            this.userService.setPlan(currentUser, "FREE");
            System.out.println("Plan updated successfully!");
            userSettingsMenu();
        });
        planMenu.setHandler(2, () -> {
            this.userService.setPlan(currentUser, "PLUS");
            System.out.println("Plan updated successfully!");
            userSettingsMenu();
        });
        planMenu.setHandler(3, () -> {
            this.userService.setPlan(currentUser, "PREMIUM");
            System.out.println("Plan updated successfully!");
            userSettingsMenu();
        });

        planMenu.run();
    }

    /**
     * User info.
     */
    private void userInfoMenu() {
        System.out.println("== MY INFO ==");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Plan: " + currentUser.getSubscriptionPlan());
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail().toLowerCase());
        System.out.println("Address: " + currentUser.getAddress());
        System.out.println("Birth Date: " + currentUser.getBirthDate().toString());
        System.out.println("Points: " + currentUser.getPoints());
        System.out.println("Number of musics heard: " + listeningRecordService.getNumListened(currentUser));

        userSettingsMenu();
    }

    /**
     * Confirmation before deleting account.
     */
    private void deleteAccountMenu() {
        System.out.println("Are you sure you want to delete your account? THIS ACTION IS IRREVERSIBLE.");
        System.out.print("Please enter your answer (Y/N): ");
        String res = scanner.nextLine();
        switch (res) {
            case "Y":
                this.userService.removeUser(currentUser);
                logout();
                break;
            case "N":
                userMenu();
                break;
            default:
                System.out.println("Invalid option!");
                userMenu();
                break;
        }
    }

    /**
     * Explore user library.
     */
    public void exploreLibrary() {
        Menu exploreLibrary = new Menu("my library", new String[]{
            "Saved musics",
            "Saved albums",
            "Saved playlists",
            "Generate favourites list",
            "Generate a list of musics of a specific genre",
        });
        exploreLibrary.setPreCondition(4, () -> this.userService.getSubscriptionPlan(currentUser).canGenerateFavouritesList());
        exploreLibrary.setPreCondition(5, () -> this.userService.getSubscriptionPlan(currentUser).canCreateGenreList());

        exploreLibrary.setHandler(1, () -> exploreSavedMusics());
        exploreLibrary.setHandler(2, () -> exploreSavedAlbums());
        exploreLibrary.setHandler(3, () -> exploreSavedPlaylists());
        exploreLibrary.setHandler(4, () -> generateFavouritesList());
        exploreLibrary.setHandler(5, () -> generateGenreList());

        exploreLibrary.run();
    }

    /**
     * UI for generating favourites list.
     */
    private void generateFavouritesList() {
        System.out.print("== GENERATE FAVOURITES LIST ==");
        System.out.println("The Favourites List includes your most heard musics. You can generate and access this list at any time on your saved playlists list.");
        System.out.print("Enter the number of musics to include in the list: ");
        int num = 0;
        try {
            num = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        try {
            this.playlistService.generateFavouritesList(currentUser, num);
        } catch (TooFewMusicsException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("List generated successfully! You can now access it on your saved playlists list.!");
    }

    /**
     * UI for creating list of musics of a genre.
     */
    private void generateGenreList() {
        System.out.print("== GENERATE GENRE LIST ==");
        System.out.println("The Genre List only includes musics of a given genre. You can access and create these lists at any time.");
        System.out.print("Enter the playlist name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the genre of musics to include: ");
        String genre = scanner.nextLine();
        System.out.print("Enter the number of musics to include: ");
        int num = 0;
        try {
            num = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        try {
            this.playlistService.generateGenreList(name, genre, currentUser, num);
        } catch (NameAlreadyUsedException | TooFewMusicsException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("List generated successfully!");
    }

    /**
     * Intermediate method for exploring saved playlists.
     */
    private void exploreSavedPlaylists() {
        List<Playlist> savedPlaylists = this.currentUser.getLibrary().getPlaylists().stream().toList();

        if (savedPlaylists.isEmpty()) {
            System.out.println("No saved playlists found!");
            return;
        }

        printPlaylistsList(savedPlaylists);
    }

    /**
     * Intermediate method for exploring saved albums.
     */
    private void exploreSavedAlbums() {
        List<Album> savedAlbums = this.currentUser.getLibrary().getAlbums().stream().toList();

        if (savedAlbums.isEmpty()) {
            System.out.println("No saved albums found!");
            return;
        }

        printAlbumsList(savedAlbums);
    }

    /**
     * Intermediate method for exploring saved musics.
     */
    private void exploreSavedMusics() {
        List<Music> savedMusics = this.currentUser.getLibrary().getMusics().stream().toList();

        if (savedMusics.isEmpty()) {
            System.out.println("No saved musics found!");
            return;
        }

        printMusicsList(savedMusics);
    }

    /**
     * Category search menu.
     * @param category
     */
    public void categorySearchMenu(String category) {
        System.out.print("Search term: ");
        String query = scanner.nextLine();
        switch (category) {
            case "music":
                printMusicsList(this.musicService.searchByTitle(query));
                break;
            case "album":
                printAlbumsList(this.albumService.searchByTitle(query));
                break;
            case "playlist":
                printPlaylistsList(this.playlistService.searchByTitle(query));
                break;
            case "artist":
                searchArtistMenu();
                break;
            default:
                throw new UnknownCategoryException(category);
        }
    }

    /**
     * Artist search menu. Allows searching for albums and musics.
     */
    public void searchArtistMenu() {
        System.out.println("== SEARCH BY ARTIST ==");
        System.out.print("Artist: ");
        String artist = scanner.nextLine();
        List<Music> musics = this.musicService.searchByArtist(artist);
        List<Album> albums = this.albumService.searchByArtist(artist);

        Menu searchArtistMenu = new Menu("search for...", new String[]{
                "Musics",
                "Albums"
        });

        searchArtistMenu.setHandler(1, () -> printMusicsList(musics));
        searchArtistMenu.setHandler(2, () -> printAlbumsList(albums));

        searchArtistMenu.run();
    }

    /**
     * Prints a list of music, with regards to user preferences.
     * @param musics lista de músicas
     */
    public void printMusicsList(List<Music> musics) {
        if (musics.isEmpty()) {
            System.out.println("No musics found!");
            return;
        }
        List<Music> filteredMusics = musics.stream()
                .filter(m -> !(m.isExplicit() && !currentUser.wantsExplicit()))
                .toList();
        String[] musicNames = filteredMusics.stream().map(Music::getTitle).toArray(String[]::new);
        Menu musicListMenu = new Menu("found musics",musicNames);
        for (int i = 0; i < filteredMusics.size(); i++) {
            int index = i;
            musicListMenu.setHandler(index+1,() -> musicInfoMenu(filteredMusics.get(index)));
        }

        musicListMenu.run();
    }

    /**
     * Prints a list of albums.
     * @param albums lista de álbuns
     */
    public void printAlbumsList(List<Album> albums) {
        if (albums.isEmpty()) {
            System.out.println("No albums found!");
            return;
        }
        String[] albumNames = albums.stream().map(Album::toString).toArray(String[]::new);
        Menu albumListMenu = new Menu("found albums", albumNames);
        for (int i = 0; i < albums.size(); i++) {
            int index = i;
            albumListMenu.setHandler(index+1,() -> albumInfoMenu(albums.get(index)));
        }

        albumListMenu.run();
    }

    /**
     * Prints a list of playlists.
     * @param playlists lista de playlists
     */
    public void printPlaylistsList(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            System.out.println("No playlists found!");
            return;
        }
        String[] playlistsNames = playlists.stream().map(Playlist::getName).toArray(String[]::new);
        Menu playlistListMenu = new Menu("found playlists", playlistsNames);
        for (int i = 0; i < playlists.size(); i++) {
            int index = i;
            playlistListMenu.setHandler(index+1,() -> playlistInfoMenu(playlists.get(index)));
        }

        playlistListMenu.run();
    }

    /**
     * Menu with information and options related to a music
     * @param music música
     */
    public void musicInfoMenu(Music music) {
        System.out.println(music.toString());
        Menu musicMenu = new Menu("options", new String[]{
                "Listen",
                "Lyrics",
                "Add to favourites",
                "Add to playlist...",
                "Toggle explicit. Currently: " + music.isExplicit(),
                "Remove"
        });
        musicMenu.setPreCondition(1, () -> loggedIn);
        musicMenu.setPreCondition(3, () -> loggedIn);
        musicMenu.setPreCondition(4, () -> loggedIn && this.userService.getSubscriptionPlan(currentUser).canCreatePlaylist());
        musicMenu.setPreCondition(5, () -> isAdmin);
        musicMenu.setPreCondition(6, () -> isAdmin);

        musicMenu.setHandler(1, () -> {
            if (!this.userService.getSubscriptionPlan(currentUser).canListenSingleMusic()) {
                System.out.println("The current plan does not allow listening to single musics!");
                System.out.println("To listen to musics, please create a random playlist!");
                return;
            }
            System.out.println(listeningRecordService.playMusic(currentUser, music));
        });
        musicMenu.setHandler(2, () -> System.out.println(music.getLyrics()));
        musicMenu.setHandler(3, () -> {
            try {
                this.libraryService.addMusic(currentUser, music);
            } catch (MusicAlreadySavedException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Music added successfully!");
        });
        musicMenu.setHandler(4, () -> addMusicToPlaylist(music));
        musicMenu.setHandler(5, () -> {
            if (music.isExplicit()) {
                musicService.makeNormal((ExplicitMusic) music);
            } else {
                musicService.makeExplicit(music);
            }
        });
        musicMenu.setHandler(6, () -> {
            this.musicService.delete(music);
            searchMenu();
        });

        musicMenu.run();
    }

    private void addMusicToPlaylist(Music music) {
        System.out.println("== ADD MUSIC TO PLAYLIST ==");

        List<Playlist> playlists = playlistService.findByUser(currentUser);
        if (playlists.isEmpty()) {
            System.out.println("No playlists found!");
            return;
        }
        String[] playlistNames = playlists.stream().map(Playlist::getName).toArray(String[]::new);
        Menu playlistListMenu = new Menu("choose playlist", playlistNames);
        for (int i = 0; i < playlists.size(); i++) {
            int index = i;
            playlistListMenu.setHandler(index+1,() -> {
                Playlist playlist = playlists.get(index);
                if (playlistService.hasMusic(playlist, music)) {
                    System.out.println("Music already saved!");
                }
                playlist.addMusic(music);
                playlistService.save(playlist);
                System.out.println("Music added successfully!");
                exploreLibrary();
            });
        }

        playlistListMenu.run();
    }

    /**
     * Menu with information and options related to an album
     * @param album álbum
     */
    public void albumInfoMenu(Album album) {
        System.out.println(album.toString());
        Menu albumMenu = new Menu("options", new String[]{
                "Listen to album",
                "See musics",
                "Add to favourites",
                "Add music",
                "Remove"
        });
        albumMenu.setPreCondition(1, () -> loggedIn);
        albumMenu.setPreCondition(3, () -> loggedIn);
        albumMenu.setPreCondition(4, () -> isAdmin);
        albumMenu.setPreCondition(5, () -> isAdmin);

        albumMenu.setHandler(1, () -> playAlbum(album));
        albumMenu.setHandler(2, () -> printMusicsList(album.getMusics()));
        albumMenu.setHandler(3, () -> {
            try {
                libraryService.addAlbum(currentUser, album);
            } catch (AlbumAlreadySavedException | NoPermissionsException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Album added successfully!");
        });
        albumMenu.setHandler(4, () -> createMusicMenu(album.getName()));
        albumMenu.setHandler(5, () -> {
            try {
                albumService.delete(album);
            } catch (AlbumNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });

        albumMenu.run();
    }

    /**
     * Reproduz uma lista de músicas genérica, podendo portanto reproduzir álbuns e playlists. Permite saltar durante a reprodução e sair a qualquer momento.
     * @param nomeLista nome da lista
     * @param music lista de músicas
     */
    public void reproduzListaMusicas(String nomeLista, List<Music> music) {
        int i = 0;
        boolean aReproduzir = true;
        Random r = new Random();
        if (music.isEmpty()) {
            System.out.println("Lista vazia!");
            return;
        }
        System.out.println("A REPRODUZIR: " + nomeLista);
        while (aReproduzir && i < music.size()) {
            Music atual = music.get(i);
            if ((atual.isExplicit() && !currentUser.wantsExplicit()) || (atual.isMultimedia() && !currentUser.querVerMultimedia())) {
                i++;
                if (i >= music.size()) {
                    System.out.println("Fim da lista de músicas!");
                    aReproduzir = false;
                }
                continue;
            }
            System.out.println("CONTROLOS DA REPRODUÇÃO:");
            System.out.println("Enter para continuar, a=Música Anterior, p=Próxima Música, r=Música Aleatória, s=Sair");

            System.out.println("\nA REPRODUZIR MÚSICA: " + atual.toString() + "\n");
            if (i+1 < music.size()) {
                System.out.println("Música seguinte: " + music.get(i+1).toString() + "\n");
            } else {
                System.out.println("Última música");
            }

            boolean saltou = false;

            String letra = atual.getLyrics();
            for (String linha : letra) {
                System.out.print(linha + " ");
                String cmd = scanner.nextLine();

                switch (cmd.toLowerCase()) {
                    case "a":
                        if (currentUser.getPlano().podeAvancarRetroceder() && i > 0) {
                            i--;
                            saltou = true;
                        } else {
                            System.out.println("O plano atual não permite voltar atrás!");
                        }
                        break;
                    case "p":
                        i++;
                        saltou = true;
                        break;
                    case "r":
                        if (music.size() > 1) {
                            int novoI;
                            do {
                                novoI = r.nextInt(music.size());
                            } while (novoI == i);
                            i = novoI;
                            saltou = true;
                        }
                        break;
                    case "s":
                        aReproduzir = false;
                        break;
                    default:
                        break;
                }
                if (cmd.toLowerCase().equals("a") && currentUser.getPlano().podeAvancarRetroceder()) {
                    break;
                }
                if (cmd.toLowerCase().equals("r") || cmd.toLowerCase().equals("s") || cmd.toLowerCase().equals("p")) {
                    break;
                }
            }

            if (!saltou) {
                i++;
                atual.registaReproducao();
                currentUser.registaReproducaoMusica(atual);
            }

            if (i >= music.size()) {
                System.out.println("\nFim da lista de músicas!\n");
                aReproduzir = false;
            }
        }
    }

    /**
     * Metodo intermédio para reprodução de um álbum
     * @param album
     */
    public void playAlbum(Album album) {
        List<Music> music = album.getMusicas().values().stream().toList();
        reproduzListaMusicas(album.getName(), music);
    }

    /**
     * Menu com informações de uma playlist. Permite ouvi-la, ver as músicas e adicionar à biblioteca
     * @param playlist playlist
     */
    public void playlistInfoMenu(Playlist playlist) {
        System.out.println(playlist.toString());
        Menu menuPlaylist = new Menu("opções", new String[]{
                "Ouvir playlist",
                "Ver músicas",
                "Adicionar à biblioteca",
                "Tornar Pública",
                "Tornar Privada",
                "Remover"
        });
        menuPlaylist.setPreCondition(1, () -> loggedIn);
        menuPlaylist.setPreCondition(3, () -> loggedIn && playlist.isPublic());
        menuPlaylist.setPreCondition(4, () -> !playlist.isPublic() && (isAdmin || playlist.getCreator().equals(currentUser)));
        menuPlaylist.setPreCondition(5, () -> playlist.isPublic() && (isAdmin || playlist.getCreator().equals(currentUser)));
        menuPlaylist.setPreCondition(6, () -> isAdmin || playlist.getCreator().equals(currentUser));

        menuPlaylist.setHandler(1, () -> reproduzPlaylist(playlist));
        menuPlaylist.setHandler(2, () -> printMusicsList(playlist.getMusics().values().stream().toList()));
        menuPlaylist.setHandler(3, () -> {
            try {
                this.modelo.adicionaPlaylistBiblioteca(currentUser, playlist);
            } catch (PlaylistAlreadySavedException | NoPermissionsException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Playlist guardada com sucesso!");
        });
        menuPlaylist.setHandler(4, () -> playlist.setIsPublic(true));
        menuPlaylist.setHandler(5, () -> playlist.setIsPublic(false));
        menuPlaylist.setHandler(6, () -> {
            try {
                this.modelo.removePlaylist(playlist);
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });

        menuPlaylist.run();
    }

    /**
     * Metodo intermédio para reprodução de uma playlist, verificando as permissões de um utilizador.
     * @param playlist
     */
    public void reproduzPlaylist(Playlist playlist) {
        if (playlist instanceof CustomPlaylist) {
            if (currentUser.getPlano().podeOuvirPlaylistConstruida()) {
                List<Music> music = new ArrayList<Music>(playlist.getMusics().values().stream().toList());
                reproduzListaMusicas(playlist.getName(), music);
            } else {
                System.out.println("O plano atual só permite ouvir playlists aleatórias!");
            }
        } else if (playlist instanceof RandomPlaylist) {
            List<Music> music = new ArrayList<Music>(playlist.getMusics().values().stream().toList());
            reproduzListaMusicas(playlist.getName(), music);
        }
    }

    private void ouvirPlaylistAleatoria() {
        System.out.println("== OUVIR PLAYLIST ALEATÓRIA ==");
        System.out.print("Introduza o nome da playlist: ");
        String nome = scanner.nextLine();
        System.out.print("Introduza o número de músicas a adicionar: ");
        int num = 0;
        try {
            num = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        RandomPlaylist pa = null;
        try {
            pa = this.modelo.geraPlaylistAleatoria(nome, num, currentUser);
        } catch (TooFewMusicsException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Playlist criada com sucesso!");

        int i = 0;
        boolean aReproduzir = true;
        Random r = new Random();
        System.out.println("\nA REPRODUZIR A PLAYLIST ALEATÓRIA");
        List<Music> music = new ArrayList<Music>(pa.getMusics().values().stream().toList());

        if (music.isEmpty()) {
            System.out.println("Lista vazia!");
            return;
        }

        while (aReproduzir && i < music.size()) {
            Music atual = music.get(i);
            if ((atual.isExplicit() && !currentUser.wantsExplicit()) || (atual.isMultimedia() && !currentUser.querVerMultimedia())) {
                i++;
                if (i >= music.size()) {
                    System.out.println("Fim da lista de músicas!");
                    aReproduzir = false;
                }
                continue;
            }
            System.out.println("CONTROLOS DA REPRODUÇÃO:");
            System.out.println("Enter para continuar, r=Música Aleatória, s=Sair");

            System.out.println("\nA REPRODUZIR MÚSICA: " + atual.toString() + "\n");

            boolean saltou = false;

            String letra = atual.getLyrics();
            for (String linha : letra) {
                System.out.print(linha + " ");
                String cmd = scanner.nextLine();

                switch (cmd.toLowerCase()) {
                    case "r":
                        int novoI = i;
                        while (novoI == i) {
                            novoI = r.nextInt(music.size());
                        }
                        i = novoI;
                        saltou = true;
                        break;
                    case "s":
                        aReproduzir = false;
                        break;
                    default:
                        break;
                }
                if (cmd.toLowerCase().equals("r") || cmd.toLowerCase().equals("s")) {
                    break;
                }
            }

            if (!saltou) {
                i++;
                atual.registaReproducao();
                currentUser.registaReproducaoMusica(atual);
            }

            if (i >= music.size()) {
                System.out.println("\nFim da lista de músicas!\n");
                aReproduzir = false;
            }
        }
    }

    /**
     * Menu que apresenta várias estatísticas de utilização da SpotifUM
     */
    public void statsMenu() {
        System.out.println("+.:+ <ESTATÍSTICAS> +.:+");
        System.out.println("Nº. de utilizadores: " + this.modelo.getTotalUtilizadores());
        System.out.println("Nº. de músicas: " + this.modelo.getTotalMusicas());
        System.out.println("Nº. de álbuns: " + this.modelo.getTotalAlbuns());
        System.out.println("Nº. de playlists públicas: " + this.modelo.getTotalPlaylists());
        System.out.println("Nº. de intérpretes: " + this.modelo.getTotalInterpretes());
        System.out.println("Música mais reproduzida: " + this.modelo.getMusicaMaisReproduzida());
        System.out.println("Intérprete mais escutado: " + this.modelo.getInterpreteMaisEscutado());
        System.out.println("User que mais músicas ouviu desde sempre: " + this.modelo.getUserMaisMusicasOuvidas(LocalDate.of(2000,1,1)));
        System.out.println("User que mais músicas ouviu no último mês: " + this.modelo.getUserMaisMusicasOuvidas(LocalDate.now().minusMonths(1)));
        System.out.println("User com mais pontos: " + this.modelo.getUserMaisPontos());
        System.out.println("Género de música mais reproduzida: " + this.modelo.getTipoMaisReproduzido());
        System.out.println("User com mais playlists: " + this.modelo.getUserMaisPlaylists());
    }

    /**
     * Menu com várias opções de administração, como criar álbuns e músicas.
     */
    public void adminMenu() {
        Menu menuAdministracao = new Menu("administração", new String[]{
            "Criar álbum",
            "Criar playlist",
            "Guardar estado",
        });
        menuAdministracao.setHandler(1, () -> menuCriarAlbum());
        menuAdministracao.setHandler(2, () -> menuCriarPlaylist());
        menuAdministracao.setHandler(3, () -> guardaEstado());

        menuAdministracao.run();
    }

    /**
     * UI para criar uma playlist.
     */
    public void menuCriarPlaylist() {
        if (!currentUser.getPlano().canCreatePlaylist()) {
            System.out.println("O plano atual não permite criar playlists!");
            return;
        }
        System.out.println("*+.:*+.<CRIAR PLAYLIST>.+*:.+*");
        System.out.print("Nome da playlist: ");
        String nome = scanner.nextLine();
        try {
            this.modelo.criaPlaylist(nome, currentUser);
        } catch (NameAlreadyUsedException | NoPermissionsException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Playlist '" + nome + "' criada!");
    }

    /**
     * UI para criar uma música dentro de um álbum.
     * @param nomeAlbum nome do álbum à qual a música vai ser adicionada
     */
    public void createMusicMenu(String nomeAlbum) {
        System.out.println("\n+.:+ <ADICIONAR MÚSICA> +.:+");

        System.out.print("Introduza o nome da música: ");
        String nome = scanner.nextLine();
        System.out.print("Introduza o nome do intérprete: ");
        String interprete = scanner.nextLine();
        System.out.print("Introduza o nome da editora: ");
        String editora = scanner.nextLine();
        System.out.print("Introduza o género da música: ");
        String genero = scanner.nextLine();
        System.out.print("Introduza a duração da música (em segundos): ");
        int duracao = 0;
        try {
            duracao = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Leitura do ficheiro que contém a letra da música:");
        List<String> letra = carregaTexto();
        System.out.println("Leitura do ficheiro que contém os caracteres da música:");
        List<String> caracteres = carregaTexto();

        try {
            this.modelo.adicionaMusica(nomeAlbum, nome, interprete, editora, genero, duracao, letra, caracteres);
        } catch (NameAlreadyUsedException e) {
            System.out.println("Música com o nome " + nome + " já existe!");
        }

        System.out.println("Music '" + nome + "' adicionada!");
    }

    /**
     * UI para criar um álbum
     */
    public void menuCriarAlbum() {
        System.out.println("\n+.:+ <ADICIONAR ÁLBUM> +.:+");
        System.out.print("Introduza o título do álbum: ");
        String nome = scanner.nextLine();
        System.out.print("Introduza o nome do intérprete: ");
        String interprete = scanner.nextLine();
        System.out.print("Introduza o nome da editora: ");
        String editora = scanner.nextLine();
        System.out.print("Introduza o ano de lançamento: ");
        int ano = 0;
        try {
            ano = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }

        scanner.nextLine();
        try {
            this.modelo.adicionaAlbum(nome, interprete, editora, ano);
        } catch (NameAlreadyUsedException e) {
            System.out.println("\nÁlbum com o nome " + nome + " já existe!");
            return;
        }

        System.out.println("\nAlbum '" + nome + "' adicionado!");
    }

    /**
     * Metodo auxiliar para facilitar a introdução da letra e dos caracteres de uma música.
     * @return
     */
    public List<String> carregaTexto () {
        System.out.print("Introduza o nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        List<String> linhas = new ArrayList<>();
        try {
            linhas = Files.readAllLines(Paths.get(nomeFicheiro), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return linhas;
    }
}