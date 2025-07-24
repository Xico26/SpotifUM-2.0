package io.github.xico26;

/**
 * Metodo de entrada na aplicação. Apenas cria um modelo e passa-o ao controlador.
 */
public class Main {
    public static void main(String[] args) {
        SpotifUM modelo = new SpotifUM();
        Controller controller = new Controller(modelo);
        controller.run();
    }
}