package io.github.xico26.spotifum2.model;

import io.github.xico26.spotifum2.exceptions.*;
import io.github.xico26.spotifum2.model.entity.*;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.music.ExplicitMusic;
import io.github.xico26.spotifum2.model.entity.playlist.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe que funciona como modelo da aplicação. Contém lista de álbuns e utilizadores, servindo como ponto de entrada para todos os outros dados.
 */
public class SpotifUM implements Serializable {
    private Map<String, User> utilizadores;
    private Map<String, Album> albuns;
    private static final Random random = new Random();

    /**
     * Adiciona uma música aos favoritos de um utilizador.
     *
     * @param user   utilizador
     * @param music música
     * @throws MusicAlreadySavedException caso a música já esteja guardada
     * @throws NoPermissionsException    caso o utilizador não tenha permissões para o fazer
     */
    public void adicionaMusicaFavorita(User user, Music music) throws MusicAlreadySavedException, NoPermissionsException {
        if (!user.getPlano().canSaveAlbum()) {
            throw new NoPermissionsException("O plano atual não permite efetuar esta ação!");
        }
        user.getLibrary().adicionarMusica(music);
    }

    /**
     * Devolve a música mais reproduzida.
     *
     * @return música mais reproduzida
     */
    public Music getMusicaMaisReproduzida() {
        List<Music> music = new ArrayList<Music>();
        for (Album album : this.albuns.values()) {
            music.addAll(album.getMusicas().values());
        }

        return music.stream().max(Comparator.comparing(Music::getNumPlays)).orElse(null);
    }

    /**
     * Devolve o intérprete mais ouvido.
     *
     * @return o intérprete mais ouvido
     */
    public String getInterpreteMaisEscutado() {
        Map<String, Integer> mapa = new HashMap<String, Integer>();
        for (Album album : this.albuns.values()) {
            for (Music music : album.getMusicas().values()) {
                if (!mapa.containsKey(music.getInterprete())) {
                    mapa.put(music.getInterprete(), 1);
                } else {
                    mapa.merge(music.getInterprete(), 1, Integer::sum);
                }
            }
        }

        return mapa.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Devolve o utilizador que mais músicas ouviu após uma determinada data
     *
     * @param apos data
     * @return utilizador
     */
    public User getUserMaisMusicasOuvidas(LocalDate apos) {
        User user = null;
        int max = 0;
        for (User u : this.utilizadores.values()) {
            int numMusicas = 0;
            for (List<LocalDateTime> datas : u.getListeningHistory().values()) {
                numMusicas += (int) datas.stream().filter(d -> d.isAfter(apos.atStartOfDay())).count();
            }
            if (numMusicas > max) {
                max = numMusicas;
                user = u;
            }
        }
        return user;
    }

    /**
     * Devolve o utilizador com mais pontos.
     *
     * @return utilizador com mais pontos
     */
    public User getUserMaisPontos() {
        return this.utilizadores.values().stream()
                .max(Comparator.comparing(User::getPoints))
                .orElse(null);
    }

    /**
     * Devolve o género de música mais reproduzido
     *
     * @return género mais reproduzido
     */
    public String getTipoMaisReproduzido() {
        Map<String, Integer> mapa = new HashMap<String, Integer>();
        for (Album album : this.albuns.values()) {
            for (Music music : album.getMusicas().values()) {
                if (!mapa.containsKey(music.getGenre())) {
                    mapa.put(music.getGenre(), 1);
                } else {
                    mapa.merge(music.getGenre(), 1, Integer::sum);
                }
            }
        }

        return mapa.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Devolve o utilizador com mais playlists criadas.
     *
     * @return utilizador com mais playlists criadas
     */
    public User getUserMaisPlaylists() {
        User user = null;
        int max = 0;
        for (User u : this.utilizadores.values()) {
            Library b = u.getLibrary();
            int num = (int) b.getPlaylists().entrySet().stream().filter(p -> p.getValue().getCriador().equals(u)).count();

            if (num > max) {
                max = num;
                user = u;
            }
        }
        return user;
    }

    /**
     * Implementa a pesquisa de músicas.
     *
     * @param query query
     * @return lista de músicas encontradas
     */
    public List<Music> pesquisaMusicas(String query) {
        List<Music> music = new ArrayList<Music>();
        for (Album album : this.albuns.values()) {
            for (Music m : album.getMusicas().values()) {
                if (m.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    music.add(m);
                }
            }
        }
        return music;
    }

    /**
     * Implementa a pesquisa de álbuns.
     *
     * @param query query
     * @return lista de álbuns encontrados
     */
    public List<Album> pesquisaAlbuns(String query) {
        List<Album> albuns = new ArrayList<Album>();
        for (Album album : this.albuns.values()) {
            if (album.getName().toLowerCase().contains(query.toLowerCase())) {
                albuns.add(album);
            }
        }
        return albuns;
    }

    /**
     * Implementa a pesquisa de playlists
     *
     * @param query query
     * @return lista de playlists públicas encontradas
     */
    public List<Playlist> pesquisaPlaylists(String query) {
        List<Playlist> playlists = new ArrayList<Playlist>();
        for (User user : this.utilizadores.values()) {
            for (Playlist p : user.getLibrary().getPlaylists().values()) {
                if (p.getName().toLowerCase().contains(query.toLowerCase()) && p.isPublic()) {
                    playlists.add(p);
                }
            }
        }
        return playlists;
    }

    /**
     * Implementa a pesquisa de músicas por intérprete
     *
     * @param interprete intérprete
     * @return lista de músicas encontradas
     */
    public List<Music> pesquisaMusicasInterprete(String interprete) {
        List<Music> music = new ArrayList<Music>();
        for (Album album : this.albuns.values()) {
            for (Music m : album.getMusicas().values()) {
                if (m.getInterprete().toLowerCase().contains(interprete.toLowerCase())) {
                    music.add(m);
                }
            }
        }
        return music;
    }

    /**
     * Implementa a pesquisa de álbuns por intérprete
     *
     * @param interprete intérprete
     * @return lista de álbuns encontrados
     */
    public List<Album> pesquisaAlbunsInterprete(String interprete) {
        List<Album> albuns = new ArrayList<Album>();
        for (Album album : this.albuns.values()) {
            if (album.getInterprete().toLowerCase().contains(interprete.toLowerCase())) {
                albuns.add(album);
            }
        }
        return albuns;
    }

    /**
     * Adiciona um álbum aos favoritos de um utilizador.
     *
     * @param user  utilizador
     * @param album álbum
     * @throws AlbumAlreadySavedException caso o álbum já esteja guardado
     * @throws NoPermissionsException   caso o utilizador não tenha permissões para o fazer
     */
    public void adicionaAlbumFavorito(User user, Album album) throws AlbumAlreadySavedException, NoPermissionsException {
        if (!user.getPlano().canSaveAlbum()) {
            throw new NoPermissionsException("O plano atual não permite efetuar esta ação!");
        }
        if (user.getLibrary().getAlbums().containsKey(album.getName())) {
            throw new AlbumAlreadySavedException("O álbum já está guardado!");
        }
        user.getLibrary().adicionarAlbum(album);
    }

    /**
     * Diz se existe uma música no sistema
     *
     * @param music música
     * @return álbum onde está guardada
     * @throws MusicNotFoundException caso não exista
     */
    public Album existeMusica(Music music) throws MusicNotFoundException {
        for (Album album : this.albuns.values()) {
            if (album.getMusicas().containsKey(music.getTitle())) {
                return album;
            }
        }
        throw new MusicNotFoundException(music.getTitle());
    }

    /**
     * Implementa a criação de playlists.
     *
     * @param nome nome da playlist
     * @param u    criador
     * @throws NameAlreadyUsedException  caso o nome tenha sido usado
     * @throws NoPermissionsException caso o utilizador não tenha permissões
     */
    public void criaPlaylist(String nome, User u) throws NameAlreadyUsedException, NoPermissionsException {
        if (!u.getPlano().canCreatePlaylist()) {
            throw new NoPermissionsException("O plano atual não permite efetuar esta ação!");
        }
        if (u.getLibrary().getPlaylists().containsKey(nome)) {
            throw new NameAlreadyUsedException("Já existe uma playlist com o nome " + nome);
        }
        Playlist novaPlaylist = new CustomPlaylist(nome, u);
        u.getLibrary().adicionarPlaylist(novaPlaylist);
    }

    /**
     * Gera uma playlist temporária com músicas aleatórias
     *
     * @param nome          nome da playlist
     * @param numMaxMusicas nº máximo de músicas
     * @param user          utilizador
     * @return
     */
    public RandomPlaylist geraPlaylistAleatoria(String nome, int numMaxMusicas, User user) throws TooFewMusicsException {
        RandomPlaylist pa = new RandomPlaylist(nome, user);
        List<Album> as = this.albuns.values().stream().toList();
        int totalMusicas = getTotalMusicas();
        if (totalMusicas == 0) {
            throw new TooFewMusicsException("Não existem músicas suficientes para gerar uma lista aleatória!");
        }
        if (numMaxMusicas > totalMusicas) {
            numMaxMusicas = totalMusicas;
        }
        while (pa.getMusics().size() < numMaxMusicas) {
            int r1 = random.nextInt(as.size());
            Album album = as.get(r1);
            List<Music> ms = album.getMusicas().values().stream().toList();
            if (ms.isEmpty()) {
                continue;
            }
            int r2 = random.nextInt(ms.size());
            Music music = ms.get(r2);
            if (!pa.getMusics().containsKey(music.getTitle())) {
                pa.addMusic(music);
            }
        }

        return pa;
    }

    /**
     * Adiciona uma música ao sistema
     *
     * @param nomeAlbum  nome do álbum ao qual a música vai ser adicionada
     * @param nome       nome da música
     * @param interprete intérprete
     * @param editora    editora
     * @param genero     género
     * @param duracao    duração
     * @param letra      letra da música
     * @param caracteres caracteres
     * @throws NameAlreadyUsedException caso já exista uma música com o nome
     */
    public void adicionaMusica(String nomeAlbum, String nome, String interprete, String editora, String genero, int duracao, List<String> letra, List<String> caracteres) throws NameAlreadyUsedException {
        if (this.albuns.get(nomeAlbum).getMusicas().containsKey(nome)) {
            throw new NameAlreadyUsedException(nome);
        }
        Music m = new Music(nome, interprete, genero, editora, letra, caracteres, duracao);
    }

    /**
     * Adiciona um álbum ao sistema
     *
     * @param nome       nome do álbum
     * @param interprete intérprete
     * @param editora    editora
     * @param ano        ano de lançamento
     * @throws NameAlreadyUsedException caso já exista um álbum com o nome
     */
    public void adicionaAlbum(String nome, String interprete, String editora, int ano) throws NameAlreadyUsedException {
        if (this.albuns.containsKey(nome)) {
            throw new NameAlreadyUsedException(nome);
        }

        Album a = new Album(nome, interprete, editora, ano);
        this.albuns.put(nome, a);
    }

    /**
     * Remove um álbum do sistema
     *
     * @param album álbum a remover
     * @throws AlbumNotFoundException caso o álbum não exista
     */
    public void removeAlbum(Album album) throws AlbumNotFoundException {
        if (!this.albuns.containsKey(album.getName())) {
            throw new AlbumNotFoundException(album.getName());
        }
        removeMusicas(album);
        this.albuns.remove(album.getName());
        for (User u : this.utilizadores.values()) {
            if (u.getLibrary().getAlbums().containsKey(album.getName())) {
                u.getLibrary().removerAlbum(album.getName());
            }
        }
    }

    /**
     * Remove músicas de um álbum
     *
     * @param album álbum
     */
    private void removeMusicas(Album album) {
        for (Music m : album.getMusicas().values()) {
            removeMusicaUsers(m);
        }
        album.getMusicas().clear();
    }

    /**
     * Remove músicas das bibliotecas dos utilizadores para evitar problemas ao remover uma música/álbum
     *
     * @param m música
     */
    private void removeMusicaUsers(Music m) {
        for (User u : this.utilizadores.values()) {
            if (u.getLibrary().getMusicas().containsKey(m.getTitle())) {
                u.getLibrary().getMusicas().remove(m.getTitle());
            }
            for (Playlist p : u.getLibrary().getPlaylists().values()) {
                if (p.getMusics().containsKey(m.getTitle())) {
                    p.getMusics().remove(m.getTitle());
                }
            }
        }
    }

    /**
     * Adiciona uma playlist às favoritas
     *
     * @param user     utilizador
     * @param playlist playlist
     * @throws PlaylistAlreadySavedException caso a playlist já esteja guardada
     * @throws NoPermissionsException      caso o utilizador não tenha permissões
     */
    public void adicionaPlaylistBiblioteca(User user, Playlist playlist) throws PlaylistAlreadySavedException, NoPermissionsException {
        if (!user.getPlano().canSavePlaylist()) {
            throw new NoPermissionsException("O plano atual não permite efetuar esta ação!");
        }
        if (user.getLibrary().getPlaylists().containsKey(playlist.getName())) {
            throw new PlaylistAlreadySavedException("Uma playlist com o mesmo nome já está guardada!");
        }
        user.getLibrary().adicionarPlaylist(playlist);
    }

    /**
     * Remove uma playlist do sistema
     *
     * @param playlist playlist
     * @throws UserNotFoundException caso o utilizador que a criou não exista
     */
    public void removePlaylist(Playlist playlist) throws UserNotFoundException {
        User criador = this.utilizadores.get(playlist.getCreator().getName());
        if (criador == null) {
            throw new UserNotFoundException("User não encontrado!");
        }
        if (!criador.getLibrary().getPlaylists().containsKey(playlist.getName())) {
            throw new PlaylistNaoExisteException("Playlist não encontrada!");
        }
        criador.getLibrary().removerPlaylist(playlist.getName());
        for (User u : this.utilizadores.values()) {
            if (u.getLibrary().getPlaylists().containsKey(playlist.getName())) {
                u.getLibrary().removerPlaylist(playlist.getName());
            }
        }
    }

    /**
     * Torna uma música explícita
     *
     * @param music música
     */
    public void tornaExplicita(Music music) {
        ExplicitMusic me = new ExplicitMusic(music);
        substituiMusica(music, me);
    }

    public void substituiMusica(Music original, Music nova) throws MusicNotFoundException {
        Album album = existeMusica(original);
        album.getMusicas().remove(original.getTitle());
        album.getMusicas().put(nova.getTitle(), nova.clone());
    }
}