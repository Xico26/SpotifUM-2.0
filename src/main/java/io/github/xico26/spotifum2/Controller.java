package io.github.xico26.spotifum2;

import io.github.xico26.spotifum2.exceptions.*;
import io.github.xico26.spotifum2.model.*;
import io.github.xico26.spotifum2.model.entity.*;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.plan.FreePlan;
import io.github.xico26.spotifum2.model.entity.plan.PlusPlan;
import io.github.xico26.spotifum2.model.entity.plan.PremiumPlan;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;
import io.github.xico26.spotifum2.model.entity.playlist.RandomPlaylist;
import io.github.xico26.spotifum2.model.entity.playlist.CustomPlaylist;

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
    private SpotifUM modelo;
    private boolean loggedIn = false;
    private User currentUser;
    private boolean isAdmin = false;

    /**
     * Construtor parametrizado de modelo. Aceita o modelo
     * @param modelo modelo
     */
    public Controller(SpotifUM modelo) {
        this.modelo = modelo;
    }

    /**
     * Menu inicial, invocado ao iniciar a aplicação. Permite começar com um estado vazio, carregar um estado, ou guardar um estado e sair.
     */
    public void run () {
        Menu menuInicial = new Menu("menu inicial", new String[] {
                "Começar em branco",
                "Carregar estado",
                "Guardar Estado & Sair",
        });

        menuInicial.setHandler(1, () -> menuPrincipal());
        menuInicial.setHandler(2, () -> carregaEstado());
        menuInicial.setHandler(3, () -> {
            guardaEstado();
            System.exit(0);
        });

        menuInicial.run();
    }

    /**
     * Menu principal, o ponto de partida para todas as operações que podem ser realizadas. Permite pesquisar, ouvir playlists aleatórias, criar playlists, fazer login/signup, e visualizar estatísticas.
     */
    private void menuPrincipal() {
        Menu menuPrincipal = new Menu("menu principal", new String[] {
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
        menuPrincipal.setPreCondition(2, () -> loggedIn);
        menuPrincipal.setPreCondition(3, () -> loggedIn);
        menuPrincipal.setPreCondition(4, () -> loggedIn);
        menuPrincipal.setPreCondition(5, () -> loggedIn);
        menuPrincipal.setPreCondition(6, () -> (loggedIn && isAdmin));
        menuPrincipal.setPreCondition(7, () -> loggedIn);
        menuPrincipal.setPreCondition(8, () -> !loggedIn);
        menuPrincipal.setPreCondition(9, () -> !loggedIn);

        // Handlers
        menuPrincipal.setHandler(1, () -> menuPesquisar());
        menuPrincipal.setHandler(2, () -> ouvirPlaylistAleatoria());
        menuPrincipal.setHandler(3, () -> menuCriarPlaylist());
        menuPrincipal.setHandler(4, () -> menuUtilizador());
        menuPrincipal.setHandler(5, () -> menuEstatisticas());
        menuPrincipal.setHandler(6, () -> menuAdministracao());
        menuPrincipal.setHandler(7, () -> logout());
        menuPrincipal.setHandler(8, () -> login());
        menuPrincipal.setHandler(9, () -> signup());
      
        menuPrincipal.run();
    }

    /**
     * Menu que serve como UI para geração de playlist aleatória, que pode depois é reproduzida.
     */
    private void ouvirPlaylistAleatoria() {
        System.out.println("+.:+ <OUVIR PLAYLIST ALEATÓRIA> +.:+");
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
        } catch (PoucasMusicasException e) {
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
     * UI para efetuar login.
     */
    public void login() {
        User res = null;
        int tentativas = 0;
        do {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            try {
                res = this.modelo.login(username, password);
            } catch (LoginInvalidoException e) {
                System.out.println(e.getMessage());
            }
            tentativas++;
            if (tentativas == 3) {
                System.out.println("Demasiadas tentativas!");
                return;
            }
        } while (res == null);
        this.loggedIn = true;
        this.currentUser = res;
        this.isAdmin = res.isAdmin();
        System.out.println("Login efetuado com sucesso! Bem vindo, " + currentUser.getName() + "!");
        menuPrincipal();
    }

    /**
     * Implementa o logout
     */
    public void logout() {
        this.loggedIn = false;
        this.isAdmin = false;
        this.currentUser = null;
        System.out.println("Adeus!");
        menuPrincipal();
    }

    /**
     * UI para criar uma conta.
     */
    public void signup() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();
        System.out.println("Data de Nascimento");
        int dia = 0;
        int mes = 0;
        int ano = 0;
        LocalDate dataNascimento = LocalDate.of(2005,1,1);
        try {
            System.out.print("Dia: ");
            dia = scanner.nextInt();
            System.out.print("Mês: ");
            mes = scanner.nextInt();
            System.out.print("Ano: ");
            ano = scanner.nextInt();
            dataNascimento = LocalDate.of(ano, mes, dia);
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        } catch (DateTimeException e) {
            System.out.println("Data inválida!");
            return;
        }
        scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();


        try {
            this.modelo.criaUtilizador(nome, username, email, morada, dataNascimento, password);
        } catch (UsernameJaUsadoException | EmailJaUsadoException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Conta criada com sucesso! Pode agora efetuar o login.");
    }

    /**
     * Menu de pesquisa. Permite pesquisar por músicas, álbuns, playlists e intérpretes.
     */
    public void menuPesquisar() {
        Menu menuPesquisar = new Menu("pesquisar...", new String[]{
            "Pesquisar por música",
            "Pesquisar por álbum",
            "Pesquisar por playlist",
            "Pesquisar por intérprete",
        });
        menuPesquisar.setHandler(1, () -> menuPesquisarCategoria("musica"));
        menuPesquisar.setHandler(2, () -> menuPesquisarCategoria("album"));
        menuPesquisar.setHandler(3, () -> menuPesquisarCategoria("playlist"));
        menuPesquisar.setHandler(4, () -> menuPesquisarCategoria("interprete"));

        menuPesquisar.run();
    }

    /**
     * Perfil de um utilizador. Permite explorar a biblioteca e aceder às suas definições, para além de poder efetuar logout.
     */
    public void menuUtilizador() {
        Menu menuUtilizador = new Menu("o meu perfil", new String[]{
            "Explorar biblioteca",
            "Definições",
            "Logout",
        });
        menuUtilizador.setHandler(1, () -> explorarBiblioteca());
        menuUtilizador.setHandler(2, () -> menuDefinicoesUtilizador());
        menuUtilizador.setHandler(3, () -> logout());

        menuUtilizador.run();
    }

    /**
     * Menu que contém as definições de um utilizador, como atualizar o plano, mostrar / esconder músicas e apagar a conta.
     */
    private void menuDefinicoesUtilizador() {
        Menu menuDefinicoes = new Menu("as minhas definições", new String[] {
                "Atualizar plano",
                "Apagar histórico",
                "As minhas informações",
                "Mostrar Música Explícita",
                "Mostrar Música Multimédia",
                "Deixar de mostrar Música Multimédia",
                "Deixar de mostrar Música Multimédia",
                "Passar a administrador",
                "Apagar conta"
        });

        menuDefinicoes.setPreCondition(4, () -> !this.currentUser.wantsExplicit());
        menuDefinicoes.setPreCondition(5, () -> !this.currentUser.querVerMultimedia());
        menuDefinicoes.setPreCondition(6, () -> this.currentUser.querVerMultimedia());
        menuDefinicoes.setPreCondition(7, () -> this.currentUser.querVerMultimedia());
        menuDefinicoes.setPreCondition(8, () -> !isAdmin);

        menuDefinicoes.setHandler(1, () -> menuAtualizarPlano());
        menuDefinicoes.setHandler(2, () -> this.modelo.apagaHistorico(currentUser));
        menuDefinicoes.setHandler(3, () -> menuInformacoes());
        menuDefinicoes.setHandler(4, () -> this.currentUser.setWantsExplicit(true));
        menuDefinicoes.setHandler(5, () -> this.currentUser.setQuerVerMultimedia(true));
        menuDefinicoes.setHandler(6, () -> this.currentUser.setQuerVerMultimedia(false));
        menuDefinicoes.setHandler(7, () -> this.currentUser.setQuerVerMultimedia(false));
        menuDefinicoes.setHandler(8, () -> {
            this.currentUser.setIsAdmin(true);
            this.isAdmin = true;
        });
        menuDefinicoes.setHandler(9, () -> menuApagarConta());

        menuDefinicoes.run();
    }

    /**
     * UI para seleção do novo plano.
     */
    private void menuAtualizarPlano() {
        Menu menuPlanos = new Menu("atualizar plano", new String[] {
                "Base",
                "Premium",
                "Premium Top",
        });

        menuPlanos.setPreCondition(1, () -> !(currentUser.getPlano() instanceof FreePlan));
        menuPlanos.setPreCondition(2, () -> !(currentUser.getPlano() instanceof PlusPlan));
        menuPlanos.setPreCondition(3, () -> !(currentUser.getPlano() instanceof PremiumPlan));

        menuPlanos.setHandler(1, () -> {
            this.modelo.atualizaPlano(currentUser, new FreePlan());
            System.out.println("Plano atualizado com sucesso!");
            menuDefinicoesUtilizador();
        });
        menuPlanos.setHandler(2, () -> {
            this.modelo.atualizaPlano(currentUser, new PlusPlan());
            System.out.println("Plano atualizado com sucesso!");
            menuDefinicoesUtilizador();
        });
        menuPlanos.setHandler(3, () -> {
            this.modelo.atualizaPlano(currentUser, new PremiumPlan());
            System.out.println("Plano atualizado com sucesso!");
            menuDefinicoesUtilizador();
        });

        menuPlanos.run();
    }

    /**
     * Informações de um utilizador. Imprime o nome, plano, número de pontos, e outras informações.
     */
    private void menuInformacoes() {
        System.out.println("+.:+ <AS MINHAS INFORMAÇÕES> +.:+");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Plano: " + currentUser.getPlano().toString().toUpperCase());
        System.out.println("Nome: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail().toLowerCase());
        System.out.println("Morada: " + currentUser.getAddress());
        System.out.println("Data de Nascimento: " + currentUser.getBirthDate().toString());
        System.out.println("Pontos: " + currentUser.getPoints());
        System.out.println("Nº. de músicas ouvidas: " + currentUser.getNumMusicasOuvidas());

        menuDefinicoesUtilizador();
    }

    /**
     * Confirmação antes de apagar conta.
     */
    private void menuApagarConta() {
        System.out.print("Tem a certeza de que quer apagar a conta? (S/N)" );
        String res = scanner.nextLine();
        switch (res) {
            case "S":
                this.modelo.apagaConta(currentUser);
                logout();
                break;
            case "N":
                menuUtilizador();
                break;
            default:
                System.out.println("Resposta inválida!");
                menuUtilizador();
                break;
        }
    }

    /**
     * Menu de exploração da biblioteca de um utilizador. Permite explorar músicas, álbuns, playlists, e gerar playlists especiais.
     */
    public void explorarBiblioteca() {
        Menu explorarBiblioteca = new Menu("a minha biblioteca", new String[]{
            "Ver músicas",
            "Ver álbuns",
            "Ver playlists",
            "Gerar lista de favoritos",
            "Gerar lista de músicas de género específico",
        });
        explorarBiblioteca.setPreCondition(4, () -> currentUser.getPlano().podeGerarListaFavoritos());
        explorarBiblioteca.setPreCondition(5, () -> currentUser.getPlano().podeCriarListaGenero());

        explorarBiblioteca.setHandler(1, () -> explorarMusicas());
        explorarBiblioteca.setHandler(2, () -> explorarAlbuns());
        explorarBiblioteca.setHandler(3, () -> explorarPlaylists());
        explorarBiblioteca.setHandler(4, () -> gerarListaFavoritos());
        explorarBiblioteca.setHandler(5, () -> gerarListaGeneroTempo());

        explorarBiblioteca.run();
    }

    /**
     * UI para criação de uma lista de favoritos.
     */
    private void gerarListaFavoritos() {
        System.out.print("+.:+ <GERAR LISTA DE FAVORITOS> +.:+");
        System.out.println("A lista de favoritos inclui as músicas que mais ouviu. Pode aceder a esta lista a qualquer momento na lista de playlists guardadas, e pode também gerar uma nova lista a qualquer momento.");
        System.out.print("Introduza o número de músicas a incluir na lista: ");
        int num = 0;
        try {
            num = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        try {
            this.modelo.geraListFavoritos(currentUser, num);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Lista gerada com sucesso! Pode agora aceder à mesma na lista das playlists guardadas!");
    }

    /**
     * UI para criação de uma lista de género e tempo.
     */
    private void gerarListaGeneroTempo() {
        System.out.print("+.:+ <GERAR LISTA DE MÚSICAS DE UM GÉNERO> +.:+");
        System.out.println("A lista de músicas de um género inclui músicas de um dado género com duração inferior a um dado valor. Pode aceder a esta lista a qualquer momento na lista de playlists guardadas, e pode também gerar uma nova lista a qualquer momento.");
        System.out.print("Introduza o nome a dar à lista: ");
        String nome = scanner.nextLine();
        System.out.print("Introduza o género das músicas a incluir: ");
        String genero = scanner.nextLine();
        System.out.println("Introduza o número máximo de minutos que as músicas devem de ter: ");
        int minutos = 0;
        try {
            minutos = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.print("Introduza o número de músicas a incluir na lista: ");
        int num = 0;
        try {
            num = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input inválido!");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        try {
            this.modelo.geraListaGeneroTempo(nome, genero, minutos * 60, currentUser, num);
        } catch (NomeJaExisteException | PoucasMusicasException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Lista gerada com sucesso! Pode agora aceder à mesma na lista das playlists guardadas!");
    }

    /**
     * Metodo intermédio para explorar playlists guardadas.
     */
    private void explorarPlaylists() {
        List<Playlist> playlistsGuardadas = new ArrayList<Playlist>(this.currentUser.getLibrary().getPlaylists().values());

        if (playlistsGuardadas.isEmpty()) {
            System.out.println("Sem playlists guardadas!");
            return;
        }

        imprimeListaPlaylists(playlistsGuardadas);
    }

    /**
     * Metodo intermédio para explorar álbuns guardados.
     */
    private void explorarAlbuns() {
        List<Album> albunsGuardados = new ArrayList<Album>(this.currentUser.getLibrary().getAlbums().values());

        if (albunsGuardados.isEmpty()) {
            System.out.println("Sem álbuns guardados!");
            return;
        }

        imprimeListaAlbuns(albunsGuardados);
    }

    /**
     * Metodo intermédio para explorar músicas guardadas.
     */
    private void explorarMusicas() {
        List<Music> musicasGuardadas = new ArrayList<Music>(this.currentUser.getLibrary().getMusicas().values());
        if (musicasGuardadas.isEmpty()) {
            System.out.println("Sem músicas guardadas!");
            return;
        }

        imprimeListaMusicas(musicasGuardadas);
    }

    /**
     * Menu de pesquisa geral por categoria.
     * @param categoria
     */
    public void menuPesquisarCategoria (String categoria) {
        System.out.print("Termo de pesquisa: ");
        String query = scanner.nextLine();
        switch (categoria) {
            case "musica":
                imprimeListaMusicas(this.modelo.pesquisaMusicas(query));
                break;
            case "album":
                imprimeListaAlbuns(this.modelo.pesquisaAlbuns(query));
                break;
            case "playlist":
                imprimeListaPlaylists(this.modelo.pesquisaPlaylists(query));
                break;
            case "interprete":
                menuPesquisarInterprete();
                break;
            default:
                throw new CategoriaDesconhecidaException(categoria);
        }
    }

    /**
     * Menu de pesquisa por intérprete. Permite pesquisar álbuns e músicas.
     */
    public void menuPesquisarInterprete () {
        System.out.println("+.:+ <PESQUISA POR INTÉRPRETE> +.:+");
        System.out.print("Intérprete: ");
        String interprete = scanner.nextLine();
        List<Music> music = this.modelo.pesquisaMusicasInterprete(interprete);
        List<Album> albuns = this.modelo.pesquisaAlbunsInterprete(interprete);

        Menu menuPesquisaInterprete = new Menu("pesquisar por interprete...", new String[]{
                "Músicas",
                "Álbuns"
        });

        menuPesquisaInterprete.setHandler(1, () -> imprimeListaMusicas(music));
        menuPesquisaInterprete.setHandler(2, () -> imprimeListaAlbuns(albuns));

        menuPesquisaInterprete.run();
    }

    /**
     * Imprime uma lista de músicas, tendo em atenção as preferências do utilizador sobre o tipo de músicas a esconder.
     * @param music lista de músicas
     */
    public void imprimeListaMusicas(List<Music> music) {
        if (music.isEmpty()) {
            System.out.println("Nenhuma música encontrada!");
            return;
        }
        List<Music> musicasFiltradas = music.stream()
                .filter(m -> !(m.isExplicit() && !currentUser.wantsExplicit()))
                .filter(m -> !(m.isMultimedia() && !currentUser.querVerMultimedia()))
                .toList();
        String[] nomesMusicas = musicasFiltradas.stream().map(Music::getTitle).toArray(String[]::new);
        Menu menuListaMusicas = new Menu("músicas encontradas",nomesMusicas);
        for (int i = 0; i < musicasFiltradas.size(); i++) {
            int index = i;
            menuListaMusicas.setHandler(index+1,() -> menuInfoMusica(musicasFiltradas.get(index)));
        }

        menuListaMusicas.run();
    }

    /**
     * Imprime uma lista de álbuns.
     * @param albuns lista de álbuns
     */
    public void imprimeListaAlbuns(List<Album> albuns) {
        if (albuns.isEmpty()) {
            System.out.println("Nenhum álbum encontrado!");
            return;
        }
        String[] nomesAlbuns = albuns.stream().map(Album::toString).toArray(String[]::new);
        Menu menuListaAlbuns = new Menu("álbuns encontrados", nomesAlbuns);
        for (int i = 0; i < albuns.size(); i++) {
            int index = i;
            menuListaAlbuns.setHandler(index+1,() -> menuInfoAlbum(albuns.get(index)));
        }

        menuListaAlbuns.run();
    }

    /**
     * Imprime uma lista de playlists.
     * @param playlists lista de playlists
     */
    public void imprimeListaPlaylists(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            System.out.println("Nenhuma playlist encontrada!");
            return;
        }
        String[] nomesPlaylists = playlists.stream().map(Playlist::getName).toArray(String[]::new);
        Menu menuListaPlaylists = new Menu("playlists encontradas", nomesPlaylists);
        for (int i = 0; i < playlists.size(); i++) {
            int index = i;
            menuListaPlaylists.setHandler(index+1,() -> menuInfoPlaylist(playlists.get(index)));
        }

        menuListaPlaylists.run();
    }

    /**
     * Menu com informações de uma música. Permite ouvi-la, ver a letra e adicionar à biblioteca
     * @param music música
     */
    public void menuInfoMusica(Music music) {
        System.out.println(music.toString());
        Menu menuMusica = new Menu("opções", new String[]{
                "Ouvir música",
                "Ver letra",
                "Adicionar aos favoritos",
                "Adicionar à playlist...",
                "Tornar explicita",
                "Tornar multimédia",
                "Remover"
        });
        menuMusica.setPreCondition(1, () -> loggedIn);
        menuMusica.setPreCondition(3, () -> loggedIn);
        menuMusica.setPreCondition(4, () -> loggedIn && currentUser.getPlano().canCreatePlaylist());
        menuMusica.setPreCondition(5, () -> isAdmin && !music.isExplicit());
        menuMusica.setPreCondition(6, () -> isAdmin && !music.isMultimedia());
        menuMusica.setPreCondition(7, () -> isAdmin);

        menuMusica.setHandler(1, () -> {
            if (!currentUser.getPlano().podeOuvirMusicaIndividual()) {
                System.out.println("O plano atual não permite ouvir músicas de forma individual!");
                System.out.println("Para ouvir músicas, crie uma playlist aleatória!");
                return;
            }
            System.out.println(music.reproduzMusica(currentUser));
        });
        menuMusica.setHandler(2, () -> System.out.println(music.imprimeLetra()));
        menuMusica.setHandler(3, () -> {
            try {
                this.modelo.adicionaMusicaFavorita(currentUser, music);
            } catch (MusicaJaGuardadaException | SemPermissoesException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Música guardada com sucesso!");
        });
        menuMusica.setHandler(4, () -> adicionaMusicaPlaylist(music));
        menuMusica.setHandler(5, () -> this.modelo.tornaExplicita(music));
        menuMusica.setHandler(6, () -> this.modelo.tornaMultimedia(music));
        menuMusica.setHandler(7, () -> {
            this.modelo.removeMusica(music);
            menuPesquisar();
        });

        menuMusica.run();
    }

    private void adicionaMusicaPlaylist(Music music) {
        System.out.println("+.:+ <ADICIONAR MÚSICA A PLAYLIST> +.:+");

        List<Playlist> playlists = currentUser.getLibrary().getPlaylists().values().stream().filter(p -> p.getCriador().equals(currentUser)).toList();
        if (playlists.isEmpty()) {
            System.out.println("Nenhuma playlist encontrada!");
            return;
        }
        String[] nomesPlaylists = playlists.stream().map(Playlist::getName).toArray(String[]::new);
        Menu menuListaPlaylists = new Menu("escolha a playlist", nomesPlaylists);
        for (int i = 0; i < playlists.size(); i++) {
            int index = i;
            menuListaPlaylists.setHandler(index+1,() -> {
                Playlist playlist = playlists.get(index);
                if (!currentUser.getLibrary().getPlaylists().containsKey(playlist.getName())) {
                    System.out.println("A playlist não existe!");
                }
                Playlist p = currentUser.getLibrary().getPlaylists().get(playlist.getName());
                if (p.getMusics().containsKey(music.getTitle())) {
                    System.out.println("Música já guardada!");
                }
                p.adicionarMusica(music);
                System.out.println("Música adicionada com sucesso!");
                explorarBiblioteca();
            });
        }

        menuListaPlaylists.run();
    }

    /**
     * Menu com informações de um álbum. Permite ouvi-lo, ver as músicas e adicionar à biblioteca
     * @param album álbum
     */
    public void menuInfoAlbum(Album album) {
        System.out.println(album.toString());
        Menu menuAlbum = new Menu("opções", new String[]{
                "Ouvir álbum",
                "Ver músicas",
                "Adicionar aos favoritos",
                "Adicionar Música",
                "Remover"
        });
        menuAlbum.setPreCondition(1, () -> loggedIn);
        menuAlbum.setPreCondition(3, () -> loggedIn);
        menuAlbum.setPreCondition(4, () -> isAdmin);
        menuAlbum.setPreCondition(5, () -> isAdmin);

        menuAlbum.setHandler(1, () -> reproduzAlbum(album));
        menuAlbum.setHandler(2, () -> imprimeListaMusicas(album.getMusicas().values().stream().toList()));
        menuAlbum.setHandler(3, () -> {
            try {
                this.modelo.adicionaAlbumFavorito(currentUser, album);
            } catch (AlbumJaGuardadoException | SemPermissoesException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Álbum guardado com sucesso!");
        });
        menuAlbum.setHandler(4, () -> menuCriarMusica(album.getName()));
        menuAlbum.setHandler(5, () -> {
            try {
                this.modelo.removeAlbum(album);
            } catch (AlbumNaoExisteException e) {
                System.out.println(e.getMessage());
            }
        });

        menuAlbum.run();
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
    public void reproduzAlbum(Album album) {
        List<Music> music = album.getMusicas().values().stream().toList();
        reproduzListaMusicas(album.getName(), music);
    }

    /**
     * Menu com informações de uma playlist. Permite ouvi-la, ver as músicas e adicionar à biblioteca
     * @param playlist playlist
     */
    public void menuInfoPlaylist(Playlist playlist) {
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
        menuPlaylist.setHandler(2, () -> imprimeListaMusicas(playlist.getMusics().values().stream().toList()));
        menuPlaylist.setHandler(3, () -> {
            try {
                this.modelo.adicionaPlaylistBiblioteca(currentUser, playlist);
            } catch (PlaylistJaGuardadaException | SemPermissoesException e) {
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

    /**
     * Menu que apresenta várias estatísticas de utilização da SpotifUM
     */
    public void menuEstatisticas() {
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
    public void menuAdministracao() {
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
        } catch (NomeJaExisteException | SemPermissoesException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Playlist '" + nome + "' criada!");
    }

    /**
     * UI para criar uma música dentro de um álbum.
     * @param nomeAlbum nome do álbum à qual a música vai ser adicionada
     */
    public void menuCriarMusica(String nomeAlbum) {
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
        } catch (NomeJaExisteException e) {
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
        } catch (NomeJaExisteException e) {
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

    /**
     * Metodo que guarda o estado da aplicação num ficheiro binário.
     */
    public void guardaEstado() {
        System.out.print("Introduza o nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        try {
            FileOutputStream fos = new FileOutputStream(nomeFicheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.modelo);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado! "+ e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro ao guardar o ficheiro: " + e.getMessage());
        }
    }

    /**
     * Metodo que carrega um estado de um ficheiro binário.
     */
    public void carregaEstado() {
        System.out.println("Introduza o nome do ficheiro:");
        String nomeFicheiro = scanner.nextLine();
        try {
            FileInputStream fis = new FileInputStream(nomeFicheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpotifUM modelo = (SpotifUM) ois.readObject();
            this.modelo = modelo;
            ois.close();
            menuPrincipal();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o ficheiro: " + e.getMessage());
        }
    }
}