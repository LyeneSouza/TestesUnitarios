package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class) // Define que se trata de testes parametrizáveis
public class CalculoValorLocacaoTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO dao;
    @Mock
    private SPCService spc;

    @Parameterized.Parameter // Define que essa variável vai receber o valor no índice 0 da fonte de dados
    public List<Filme> filmes;

    @Parameterized.Parameter(value=1) // Define que essa variável vai receber o valor no índice 1 da fonte de dados
    public Double valorLocacao;

    @Parameterized.Parameter(value=2)
    public String cenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
                //openMocks(this);
    }

    private static Filme filme1 = umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = umFilme().agora();
    private static Filme filme7 = umFilme().agora();

    @Parameterized.Parameters(name = "{2}") // A anotação faz o JUnit saber que essa vai ser a fonte de dados para a execução dos testes
    // Os dados utilizados no teste devem ficar em um Array, que será a fonte de dados
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object [][] {
            {Arrays.asList(filme1, filme2), 20.0, "2 filmes: sem desconto"}, // Desse modo é mais fácil acrescentar testes em cenários parecidos
            {Arrays.asList(filme1, filme2, filme3), 27.5, "3 filmes: 25%"}, // Primeiro dado
            {Arrays.asList(filme1, filme2, filme3, filme4), 32.5, "4 filmes: 50%"}, // Segundo dado
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 35.0, "5 filmes: 75%"}, // Terceiro dado
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 35.0, "6 filmes: 100%"}, // Quarto dado
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 45.0, "7 filmes: sem desconto"} // Adicionando mais um cenário
        });
    }

    // Cada método da classe será executado uma vez para cada dado informado dentro do Array

    @Test // Teste genérico, que vai funcionar com cenários e valores diferentes, a partir do que for fornecido pela coleção de dados
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {

        // Etapa 1 - cenário
        Usuario usuario = umUsuario().agora();

        // Etapa 2 - ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        // Etapa 3 - verificar
        assertThat(locacao.getValor(), is(valorLocacao));

        System.out.println("!!");
    }
}
