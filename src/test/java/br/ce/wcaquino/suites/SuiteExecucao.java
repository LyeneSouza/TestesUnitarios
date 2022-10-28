package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class) // Anotação que faz o JUnit saber que se trata de Suíte de Testes
@Suite.SuiteClasses({ // Define todas as classes de teste que eu quero que sejam executadas nesta suíte
    CalculadoraTest.class,
    CalculoValorLocacaoTest.class,
    LocacaoServiceTest.class
})
public class SuiteExecucao {
    // Declaração de classe é obrigatória no Java; remova se puder

    @BeforeClass
    public static void before() {
        System.out.println("Before");
    }

    @AfterClass
    public static void after() {
        System.out.println("After");
    }
}
