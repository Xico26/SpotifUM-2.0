package io.github.xico26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Implementa os menus textuais usados.
 */
public class Menu {
    /**
     * Interface funcional para handlers
     */
    public interface Handler {
        public void execute();
    }

    /**
     * Interface funcional para pré-condições.
     */
    public interface PreCondition {
        public boolean validar();
    }

    private static Scanner scanner = new Scanner(System.in);

    private List<String> opcoes;
    private List<PreCondition> disponivel;
    private List<Handler> handlers;
    private String nomeMenu;

    /**
     * Construtor de menus. Aceita:
     * @param nomeMenu nome do menu a apresentar
     * @param opcoes opções
     */
    public Menu(String nomeMenu, String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s-> {
            this.disponivel.add(()->true);
            this.handlers.add(()->System.out.println("\nOpção não implementada!"));
        });
        this.nomeMenu = nomeMenu;
    }

    /**
     * Executa um menu. Trata da leitura e validação dos inputs. Termina quando é introduzido 0.
     */
    public void run() {
        int op;
        do {
            imprimeMenu();
            op = readOption();
            // testar pré-condição
            if (op>0 && !this.disponivel.get(op-1).validar()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op>0) {
                // executar handler
                this.handlers.get(op-1).execute();
            }
        } while (op != 0);
    }

    /**
     * Metodo que regista uma pré-condição numa opção do menu.
     *
     * @param i índice da opção (começa em 1)
     * @param b pré-condição a registar
     */
    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i-1,b);
    }

    /**
     * Metodo para registar um handler numa opção do menu.
     *
     * @param i indice da opção  (começa em 1)
     * @param h handlers a registar
     */
    public void setHandler(int i, Handler h) {
        this.handlers.set(i-1, h);
    }

    /**
     * Imprime o menu.
     */
    private void imprimeMenu() {
        System.out.println("+.:+ <"+ this.nomeMenu.toUpperCase() + "> +.:+");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(this.disponivel.get(i).validar()?this.opcoes.get(i):"---");
        }
        System.out.println("0. Sair");
    }

    /** Ler uma opção válida */
    private int readOption() {
        int op;
        System.out.print("Opção: ");
        try {
            String line = scanner.nextLine();
            op = Integer.parseInt(line);
        }
        catch (NumberFormatException e) { // Não foi escrito um int
            op = -1;
        }
        if (op<0 || op>this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }
}