package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Precisa ter o cuidado de deixar os testes em ordem alfabética
public class OrdemTest {

    public static int contador = 0;

    @Test
    public void inicia() {
        contador = 1;
    }

    @Test
    public void verifica() {
        Assert.assertEquals(1, contador);
    }

    // Uma forma de resolver a ordem dos testes - perde rastreabilidade
    // inicia() e verifica() deixam de ser testes e crio um único teste chamando esses métodos na ordem que eu quero
    /*@Test
    public void testeGeral() {
        inicia();
        verifica();
    }*/
}
