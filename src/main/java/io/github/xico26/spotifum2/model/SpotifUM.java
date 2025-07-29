package io.github.xico26.spotifum2.model;

import io.github.xico26.spotifum2.exceptions.*;
import io.github.xico26.spotifum2.model.entity.*;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.music.ExplicitMusic;
import io.github.xico26.spotifum2.model.entity.plan.IPlanoSubscricao;
import io.github.xico26.spotifum2.model.entity.plan.PlanoPremiumTop;
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
     * Construtor por omissão.
     */
    public SpotifUM() {
        this.utilizadores = new HashMap<String, User>();
        this.albuns = new HashMap<String, Album>();
    }

    /**
     * Construtor parametrizado. Aceita:
     *
     * @param utilizadores mapa de utilizadores
     * @param albuns       mapa de álbuns
     */
    public SpotifUM(Map<String, User> utilizadores, Map<String, Album> albuns) {
        setUtilizadores(utilizadores);
        setAlbuns(albuns);
    }

    /**
     * Construtor de cópia. Aceita:
     *
     * @param novoModelo novo modelo
     */
    public SpotifUM(SpotifUM novoModelo) {
        setUtilizadores(novoModelo.getUtilizadores());
        setAlbuns(novoModelo.getAlbuns());
    }

    /**
     * Devolve os álbuns no sistema.
     *
     * @return mapa de álbuns
     */
    public Map<String, Album> getAlbuns() {
        Map<String, Album> albunsClone = new HashMap<String, Album>();
        for (Map.Entry<String, Album> a : this.albuns.entrySet()) {
            albunsClone.put(a.getKey(), a.getValue().clone());
        }
        return albunsClone;
    }

    /**
     * Atualiza os álbuns no sistema.
     *
     * @param albuns novos álbuns
     */
    public void setAlbuns(Map<String, Album> albuns) {
        this.albuns = new HashMap<String, Album>();
        for (Map.Entry<String, Album> a : albuns.entrySet()) {
            this.albuns.put(a.getKey(), a.getValue().clone());
        }
    }

    /**
     * Devolve os utilizadores no sistema.
     *
     * @return mapa de utilizadores
     */
    public Map<String, User> getUtilizadores() {
        Map<String, User> utilizadoresClone = new HashMap<String, User>();
        for (Map.Entry<String, User> u : this.utilizadores.entrySet()) {
            utilizadoresClone.put(u.getKey(), u.getValue().clone());
        }
        return utilizadoresClone;
    }

    /**
     * Atualiza os utilizadores no sistema.
     *
     * @param utilizadores novos utilizadores
     */
    public void setUtilizadores(Map<String, User> utilizadores) {
        this.utilizadores = new HashMap<String, User>();
        for (Map.Entry<String, User> u : utilizadores.entrySet()) {
            this.utilizadores.put(u.getKey(), u.getValue().clone());
        }
    }

    /**
     * Adiciona um utilizador ao sistema.
     *
     * @param nome           nome
     * @param username       username
     * @param email          email
     * @param morada         morada
     * @param dataNascimento data de nascimento
     * @param password       password
     * @throws UsernameJaUsadoException caso username já esteja a ser usado
     * @throws EmailJaUsadoException    caso email já esteja a ser usado
     */
    public void criaUtilizador(String nome, String username, String email, String morada, LocalDate dataNascimento, String password) throws UsernameJaUsadoException, EmailJaUsadoException {
        if (this.utilizadores.containsKey(username)) {
            throw new UsernameJaUsadoException("O username " + username + " já está a ser usado!");
        }
        for (User u : this.utilizadores.values()) {
            if (u.getEmail().equals(email)) {
                throw new EmailJaUsadoException("O email " + email + " já está a ser usado!");
            }
        }
        User novoUser = new User(username, password, nome, morada, email, dataNascimento);
        this.utilizadores.put(username, novoUser);
    }

    /**
     * Lógica para login
     *
     * @param username username
     * @param password password
     * @return utilizador que iniciou sessão
     * @throws LoginInvalidoException caso username ou password não coincidam / não existam
     */
    public User login(String username, String password) throws LoginInvalidoException {
        User user = this.utilizadores.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new LoginInvalidoException("Username ou palavra passe incorreta!");
        }
        return user;
    }

    /**
     * Remove uma música do sistema
     *
     * @param music música a remover
     */
    public void removeMusica(Music music) {
        for (Album album : this.albuns.values()) {
            if (album.temMusica(music.getTitle())) {
                album.removeMusica(music.getTitle());
                removeMusicaUsers(music);
                return;
            }
        }
        throw new MusicaNaoExisteException(music.getTitle());
    }

    /**
     * Adiciona uma música aos favoritos de um utilizador.
     *
     * @param user   utilizador
     * @param music música
     * @throws MusicaJaGuardadaException caso a música já esteja guardada
     * @throws SemPermissoesException    caso o utilizador não tenha permissões para o fazer
     */
    public void adicionaMusicaFavorita(User user, Music music) throws MusicaJaGuardadaException, SemPermissoesException {
        if (!user.getPlano().podeGuardarAlbum()) {
            throw new SemPermissoesException("O plano atual não permite efetuar esta ação!");
        }
        user.getBiblioteca().adicionarMusica(music);
    }

    /**
     * Devolve o número total de utilizadores.
     *
     * @return nº de utilizadores
     */
    public int getTotalUtilizadores() {
        return this.utilizadores.size();
    }

    /**
     * Devolve o número total de álbuns.
     *
     * @return nº de álbuns
     */
    public int getTotalAlbuns() {
        return this.albuns.size();
    }

    /**
     * Devolve o número total de músicas.
     *
     * @return nº de músicas
     */
    public int getTotalMusicas() {
        int numMusicas = 0;
        for (Album album : this.albuns.values()) {
            numMusicas += album.getMusicas().size();
        }
        return numMusicas;
    }

    /**
     * Devolve o número total de playlists.
     *
     * @return nº de playlists
     */
    public int getTotalPlaylists() {
        List<String> playlists = new ArrayList<String>();
        for (User user : this.utilizadores.values()) {
            for (Playlist p : user.getBiblioteca().getPlaylists().values()) {
                if (!playlists.contains(p.getNome())) {
                    playlists.add(p.getNome());
                }
            }
        }
        return playlists.size();
    }

    /**
     * Devolve o número total de intérpretes.
     *
     * @return nº de intérpretes
     */
    public int getTotalInterpretes() {
        List<String> interpretes = new ArrayList<String>();
        for (Album album : this.albuns.values()) {
            for (Music music : album.getMusicas().values()) {
                if (!interpretes.contains(album.getInterprete())) {
                    interpretes.add(album.getInterprete());
                }
            }
        }
        return interpretes.size();
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
            Biblioteca b = u.getBiblioteca();
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
            for (Playlist p : user.getBiblioteca().getPlaylists().values()) {
                if (p.getNome().toLowerCase().contains(query.toLowerCase()) && p.isPublic()) {
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
     * @throws AlbumJaGuardadoException caso o álbum já esteja guardado
     * @throws SemPermissoesException   caso o utilizador não tenha permissões para o fazer
     */
    public void adicionaAlbumFavorito(User user, Album album) throws AlbumJaGuardadoException, SemPermissoesException {
        if (!user.getPlano().podeGuardarAlbum()) {
            throw new SemPermissoesException("O plano atual não permite efetuar esta ação!");
        }
        if (user.getBiblioteca().getAlbuns().containsKey(album.getName())) {
            throw new AlbumJaGuardadoException("O álbum já está guardado!");
        }
        user.getBiblioteca().adicionarAlbum(album);
    }

    /**
     * Diz se existe uma música no sistema
     *
     * @param music música
     * @return álbum onde está guardada
     * @throws MusicaNaoExisteException caso não exista
     */
    public Album existeMusica(Music music) throws MusicaNaoExisteException {
        for (Album album : this.albuns.values()) {
            if (album.getMusicas().containsKey(music.getTitle())) {
                return album;
            }
        }
        throw new MusicaNaoExisteException(music.getTitle());
    }

    /**
     * Implementa a criação de playlists.
     *
     * @param nome nome da playlist
     * @param u    criador
     * @throws NomeJaExisteException  caso o nome tenha sido usado
     * @throws SemPermissoesException caso o utilizador não tenha permissões
     */
    public void criaPlaylist(String nome, User u) throws NomeJaExisteException, SemPermissoesException {
        if (!u.getPlano().podeCriarPlaylist()) {
            throw new SemPermissoesException("O plano atual não permite efetuar esta ação!");
        }
        if (u.getBiblioteca().getPlaylists().containsKey(nome)) {
            throw new NomeJaExisteException("Já existe uma playlist com o nome " + nome);
        }
        Playlist novaPlaylist = new PlaylistConstruida(nome, u);
        u.getBiblioteca().adicionarPlaylist(novaPlaylist);
    }

    /**
     * Gera uma lista de favoritos, as n músicas mais ouvidas pelo utilizador.
     *
     * @param user   utilizador
     * @param limite nº de músicas a incluir
     */
    public void geraListFavoritos(User user, int limite) throws PoucasMusicasException {
        String nome = "Lista de Favoritos";
        if (user.getBiblioteca().getPlaylists().containsKey(nome)) {
            user.getBiblioteca().removerPlaylist(nome);
        }
        if (user.getNumMusicasOuvidas() < 10) {
            throw new PoucasMusicasException("Ouça pelo menos 10 músicas para poder ter acesso à lista de favoritos!");
        }

        ListaFavoritos favs = new ListaFavoritos(nome, user);
        user.getListeningHistory().entrySet().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getValue().size(), m1.getValue().size()))
                .limit(limite)
                .forEach(entry -> favs.adicionarMusica(entry.getKey()));
    }

    /**
     * Gera uma playlist temporária com músicas aleatórias
     *
     * @param nome          nome da playlist
     * @param numMaxMusicas nº máximo de músicas
     * @param user          utilizador
     * @return
     */
    public PlaylistAleatoria geraPlaylistAleatoria(String nome, int numMaxMusicas, User user) throws PoucasMusicasException {
        PlaylistAleatoria pa = new PlaylistAleatoria(nome, user);
        List<Album> as = this.albuns.values().stream().toList();
        int totalMusicas = getTotalMusicas();
        if (totalMusicas == 0) {
            throw new PoucasMusicasException("Não existem músicas suficientes para gerar uma lista aleatória!");
        }
        if (numMaxMusicas > totalMusicas) {
            numMaxMusicas = totalMusicas;
        }
        while (pa.getMusicas().size() < numMaxMusicas) {
            int r1 = random.nextInt(as.size());
            Album album = as.get(r1);
            List<Music> ms = album.getMusicas().values().stream().toList();
            if (ms.isEmpty()) {
                continue;
            }
            int r2 = random.nextInt(ms.size());
            Music music = ms.get(r2);
            if (!pa.getMusicas().containsKey(music.getTitle())) {
                pa.adicionarMusica(music);
            }
        }

        return pa;
    }

    /**
     * Gera uma lista de músicas de um dado género com duração inferior a um valor
     *
     * @param nome        nome da playlist
     * @param genero      género
     * @param tempoMaximo tempo máximo (em segundos)
     * @param u           utilizador
     * @param numMusicas  nº de músicas a incluir
     * @throws NomeJaExisteException caso o nome já esteja a ser usado
     */
    public void geraListaGeneroTempo(String nome, String genero, int tempoMaximo, User u, int numMusicas) throws NomeJaExisteException, PoucasMusicasException {
        if (u.getBiblioteca().getPlaylists().containsKey(nome)) {
            throw new NomeJaExisteException("Já existe uma playlist com o nome " + nome);
        }
        if (getTotalMusicas() == 0) {
            throw new PoucasMusicasException("Não existem músicas suficientes para gerar uma lista!");
        }
        ListaGeneroTempo lgt = new ListaGeneroTempo(nome, u);
        int i = 0;
        for (Album album : this.albuns.values()) {
            for (Music m : album.getMusicas().values()) {
                if (m.getGenre().toLowerCase().equals(genero.toLowerCase()) && m.getDuration() <= tempoMaximo) {
                    lgt.adicionarMusica(m);
                    i++;
                    if (i >= numMusicas) {
                        u.getBiblioteca().adicionarPlaylist(lgt);
                        return;
                    }
                }
            }
        }

        if (!lgt.getMusicas().isEmpty()) {
            u.getBiblioteca().adicionarPlaylist(lgt);
        }
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
     * @throws NomeJaExisteException caso já exista uma música com o nome
     */
    public void adicionaMusica(String nomeAlbum, String nome, String interprete, String editora, String genero, int duracao, List<String> letra, List<String> caracteres) throws NomeJaExisteException {
        if (this.albuns.get(nomeAlbum).getMusicas().containsKey(nome)) {
            throw new NomeJaExisteException(nome);
        }
        Music m = new Music(nome, interprete, genero, editora, letra, caracteres, duracao);
        this.albuns.get(nomeAlbum).addMusic(m.clone());
    }

    /**
     * Adiciona um álbum ao sistema
     *
     * @param nome       nome do álbum
     * @param interprete intérprete
     * @param editora    editora
     * @param ano        ano de lançamento
     * @throws NomeJaExisteException caso já exista um álbum com o nome
     */
    public void adicionaAlbum(String nome, String interprete, String editora, int ano) throws NomeJaExisteException {
        if (this.albuns.containsKey(nome)) {
            throw new NomeJaExisteException(nome);
        }

        Album a = new Album(nome, interprete, editora, ano);
        this.albuns.put(nome, a);
    }

    /**
     * Remove um álbum do sistema
     *
     * @param album álbum a remover
     * @throws AlbumNaoExisteException caso o álbum não exista
     */
    public void removeAlbum(Album album) throws AlbumNaoExisteException {
        if (!this.albuns.containsKey(album.getName())) {
            throw new AlbumNaoExisteException(album.getName());
        }
        removeMusicas(album);
        this.albuns.remove(album.getName());
        for (User u : this.utilizadores.values()) {
            if (u.getBiblioteca().getAlbuns().containsKey(album.getName())) {
                u.getBiblioteca().removerAlbum(album.getName());
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
            if (u.getBiblioteca().getMusicas().containsKey(m.getTitle())) {
                u.getBiblioteca().getMusicas().remove(m.getTitle());
            }
            for (Playlist p : u.getBiblioteca().getPlaylists().values()) {
                if (p.getMusicas().containsKey(m.getTitle())) {
                    p.getMusicas().remove(m.getTitle());
                }
            }
        }
    }

    /**
     * Adiciona uma playlist às favoritas
     *
     * @param user     utilizador
     * @param playlist playlist
     * @throws PlaylistJaGuardadaException caso a playlist já esteja guardada
     * @throws SemPermissoesException      caso o utilizador não tenha permissões
     */
    public void adicionaPlaylistBiblioteca(User user, Playlist playlist) throws PlaylistJaGuardadaException, SemPermissoesException {
        if (!user.getPlano().podeGuardarPlaylist()) {
            throw new SemPermissoesException("O plano atual não permite efetuar esta ação!");
        }
        if (user.getBiblioteca().getPlaylists().containsKey(playlist.getNome())) {
            throw new PlaylistJaGuardadaException("Uma playlist com o mesmo nome já está guardada!");
        }
        user.getBiblioteca().adicionarPlaylist(playlist);
    }

    /**
     * Remove uma playlist do sistema
     *
     * @param playlist playlist
     * @throws UserNotFoundException caso o utilizador que a criou não exista
     */
    public void removePlaylist(Playlist playlist) throws UserNotFoundException {
        User criador = this.utilizadores.get(playlist.getCriador().getName());
        if (criador == null) {
            throw new UserNotFoundException("User não encontrado!");
        }
        if (!criador.getBiblioteca().getPlaylists().containsKey(playlist.getNome())) {
            throw new PlaylistNaoExisteException("Playlist não encontrada!");
        }
        criador.getBiblioteca().removerPlaylist(playlist.getNome());
        for (User u : this.utilizadores.values()) {
            if (u.getBiblioteca().getPlaylists().containsKey(playlist.getNome())) {
                u.getBiblioteca().removerPlaylist(playlist.getNome());
            }
        }
    }

    /**
     * Apaga o histórico de músicas ouvidas de um utilizador
     *
     * @param user utilizador
     */
    public void apagaHistorico(User user) {
        user.apagaHistorico();
    }

    /**
     * Atualiza o plano de um utilizador, adicionando 100 pontos caso o novo plano seja Premium Top.
     *
     * @param user  utilizador
     * @param plano novo plano
     */
    public void atualizaPlano(User user, IPlanoSubscricao plano) {
        user.setPlano(plano);
        if (plano instanceof PlanoPremiumTop) {
            user.adicionarPontos(100);
        }
    }

    /**
     * Apaga um utilizador do sistema
     *
     * @param user utilizador
     */
    public void apagaConta(User user) {
        if (!this.utilizadores.containsKey(user.getName())) {
            throw new UserNotFoundException(user.getName());
        }
        this.utilizadores.remove(user.getName());
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

    /**
     * Torna uma música multimédia
     *
     * @param music música
     */
    public void tornaMultimedia(Music music) {
        MusicaMultimedia mm = new MusicaMultimedia(music);
        substituiMusica(music, mm);
    }

    /**
     * Substitui uma música nos álbuns após ser transformada em explícita / multimédia
     *
     * @param original música original
     * @param nova     música nova
     * @throws MusicaNaoExisteException caso a música original não exista
     */
    public void substituiMusica(Music original, Music nova) throws MusicaNaoExisteException {
        Album album = existeMusica(original);
        album.getMusicas().remove(original.getTitle());
        album.getMusicas().put(nova.getTitle(), nova.clone());
    }
}