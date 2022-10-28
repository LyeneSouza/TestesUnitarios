package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Locacao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //openMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {

        // Comportamento do Mock. Se não definir expectativa, ele não sabe o que fazer e retorna default (nulo, 0...)
        Mockito.when(calcMock.somar(1, 2)).thenReturn(8); // Definindo a expectativa
        Mockito.when(calcMock.somar(3, 2)).thenCallRealMethod(); // Definindo outra expectativa
        System.out.println("Mock 28: " + calcMock.somar(1, 2)); // Imprime 8 porque corresponde à expectativa definida
        System.out.println("Mock 29: " + calcMock.somar(2, 2)); // Imprime 0 (default) porque não sabe o que fazer
        System.out.println("Mock 30: " + calcMock.somar(3, 2)); // Imprime 5 porque a expectativa manda chamar o método real

        // Comportamento do Spy. Se não definir expectativa, ele não sabe o que fazer e retorna o valor real
        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8); // Definindo a expectativa - executa o método neste momento
        System.out.println("Spy 34: " + calcSpy.somar(1, 2)); // Imprime 8 porque corresponde à expectativa definida
        System.out.println("Spy 35: " + calcSpy.somar(2, 2)); // Imprime o resultado real porque não sabe o que fazer

        System.out.println("Mock 37: ");
        calcMock.imprime(); // O padrão do Mock para um void é não executar
        System.out.println("Spy 39: ");
        calcSpy.imprime(); // O padrão do Spy para um void é executar

        Mockito.doNothing().when(calcSpy).imprime(); // Diz para o Spy não executar o método
        System.out.println("Spy 43: ");
        calcSpy.imprime();

        // Outra forma de definir comportamento - sem executar o método na hora da expectativa
        Mockito.doReturn(5).when(calcSpy).somar(1,2);
        System.out.println("Spy 48: " + calcSpy.somar(1, 2));
    }

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5); // Se um parâmetro for Matcher, todos devem ser

        Assert.assertEquals(5, calc.somar(1, 8));
        //System.out.println(argCapt.getAllValues());
    }
}
