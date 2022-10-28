package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {
    public int somar(int a, int b) {
        System.out.println("Estou executando o método somar");
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws NaoPodeDividirPorZeroException {
        if (b == 0) {
            throw new NaoPodeDividirPorZeroException();
        }
        return a / b;
    }

    public void imprime() {
        System.out.println("Passei aqui");
    }

    // Exemplo para mostrar que, mesmo estando com 100% de cobertura, o código pode dar erro, como, por ex: se passar 0 no denominador ou se passar letras ao invés de números
    public int divide(String a, String b) {
        return Integer.valueOf(a) / Integer.valueOf(b);
    }
}
