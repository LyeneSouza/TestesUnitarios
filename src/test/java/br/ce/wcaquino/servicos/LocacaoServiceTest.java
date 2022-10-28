package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class}) // Indica quais classes vão precisar do PowerMockito
public class LocacaoServiceTest {

    @InjectMocks // Indica que os mocks serão injetados aqui
    private LocacaoService locacaoService;
    @Mock // Indica que se trata de mock
    private LocacaoDAO dao;
    @Mock
    private SPCService spc;
    @Mock
    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
                //openMocks(this); // Para versões mais novas
        locacaoService = PowerMockito.spy(locacaoService); // Criando o spy para poder mockar método privado
    }

    // Desse modo, se o teste não está esperando exceção, quem gerencia a exceção lançada é o próprio JUnit
    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {

        //Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)); // Com o PowerMockito não é mais necessário usar o Assume

        // Etapa 1 - Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        // Definindo que a, cada new Date() nas classes que foram passadas por parâmetro lá no começo com @PrepareForTest, a data será 28/10/2022
        //PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 10, 2022)); // Essa determinação só vale para este teste

        // PowerMockito definindo que, na chamada do método estático do Calendar, será a data indicada
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 28);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        calendar.set(Calendar.YEAR, 2022);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        // Etapa 2 - Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        // Etapa 3 - Verificação
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        //error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        //error.checkThat(locacao.getDataLocacao(), ehHoje());
        //error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        //error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 10, 2022)), is(true)); // Usando o PowerMockito
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 10, 2022)), is(true)); // Usando o PowerMockito
    }

    // Forma elegante - não verifica a mensagem
    @Test(expected= FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

        // Etapa 1 - cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        // Etapa 2 - ação
        locacaoService.alugarFilme(usuario, filmes);

        // Resultado esperado - receber a exceção. Se receber a exceção, o teste passa
        // Não verifica a mensagem, então, para usar com segurança, esta exceção só pode vir por este motivo
    }

    // Forma robusta - quando é preciso verificar a mensagem
    @Test
    public void deveLancarExcecaoAoAlugarFilmeComUsuarioVazio() throws FilmeSemEstoqueException { // Meu teste não espera exceção do tipo FilmeSemEstoque; se aparecer, quem vai tratar é o JUnit

        // Etapa 1 - cenário
        // Usuario usuario = umUsuario().agora(); // sempre testar sem ter a exceção para verificar se o teste não está dando falso positivo
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        // Etapa 2 - ação
        try {
            locacaoService.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    // Forma nova - verifica através da Rule
    @Test
    public void deveLancarExcecaoAoAlugarFilmeVazio() throws FilmeSemEstoqueException, LocadoraException {

        // Etapa 1 - cenário
        Usuario usuario = umUsuario().agora();
        /*List<Filme> filmes = Arrays.asList(umFilme().agora());*/
        //List<Filme> filmes = Arrays.asList();

        // Precisa declarar as exceções esperadas antes da ação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio"); // Verifica a mensagem

        // Etapa 2 - ação
        locacaoService.alugarFilme(usuario, null);
    }

    @Test
    public void deveDevolverSegundaAoAlugarSabado() throws Exception {

        //Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)); // Com o PowerMockito não é mais necessário usar o Assume

        // Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        // PowerMockito definindo que, na instância do new Date(), será a data indicada
        //PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 10, 2022));

        // PowerMockito definindo que, na chamada do método estático do Calendar, será a data indicada
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        calendar.set(Calendar.YEAR, 2022);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        // Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        // Verificação
        //assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        //assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY)); // Matcher personalizado
        assertThat(locacao.getDataRetorno(), caiNumaSegunda()); // Matcher personalizado
        //PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments(); // Verifica se o construtor foi chamado
        PowerMockito.verifyStatic(Mockito.times(2)); // Verifica se algum método estático foi chamado
        Calendar.getInstance(); // Fala para o PowerMock qual método estatico deve ter sido executado
    }

    @Test
    public void deveLancarExcecaoAoUsuarioNegativadoSPC() throws Exception {

        // Cenário
        Usuario usuario = umUsuario().agora();
        //Usuario usuario2 = umUsuario().comNome("Usuário 2").agora(); // Criar outro usuário, não negativado, para garantir que o teste está falhando quando deve falhar
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true); // Alterando o comportamento padrão do mock

        // Ação
        try {
            Locacao locacao = locacaoService.alugarFilme(usuario, filmes); // Para testar a falha, passar o usuario2, não negativado, para o teste e/ou na verificação
            // Verificação
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário negativado"));
        }

        // O teste passa se lançar a exceção com a mensagem especificada
        verify(spc).possuiNegativacao(usuario); // O teste fica mais seguro, mas o verify aqui não seria tão necessário, dificultando a manutenção
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {

        // Cenário
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuário em dia").agora(); // Gerar a falha no teste passando esse usuário na verificação
        Usuario usuario3 = umUsuario().comNome("Outro usuário atrasado").agora();
        List<Locacao> locacoes =
                Arrays.asList(
                        umLocacao().atrasada().comUsuario(usuario).agora(),
                        umLocacao().comUsuario(usuario2).agora(),
                        umLocacao().atrasada().comUsuario(usuario3).agora(),
                        umLocacao().atrasada().comUsuario(usuario3).agora());
        when(dao.obterLocacosPendentes()).thenReturn(locacoes);

        // Ação
        locacaoService.notificarAtrasos();

        // Verificação
        verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class)); // Considera qualquer usuario
        /*
        verify(email).notificarAtraso(usuario);
        verify(email, atLeastOnce()).notificarAtraso(usuario3); // Garante que o usuario3 recebeu, pelo menos 1 e-mail
        verify(email, times(2)).notificarAtraso(usuario3); // Garante que o usuario3 recebeu e-mail 2 vezes - apenas ex
        verify(email, atLeast(2)).notificarAtraso(usuario3); // Garante que o usuario3 recebeu, no mínimo, 2 vezes - apenas ex
        verify(email, atMost(5)).notificarAtraso(usuario3); // Garante que o usuario3 recebeu, no máximo, 5 vezes - apenas ex
        verify(email, never()).notificarAtraso(usuario2); // Verifica que a notificação não ocorreu
        */
        verifyNoMoreInteractions(email); // Garante que nenhum outro e-mail, além dos especificados, foi enviado

        // Nesse cenário, essa verificação não é relevante
        //verifyNoInteractions(spc); // Garante que nenhuma interação foi feita com o mock passado por parâmetro
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {

        // Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        // Verificação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com SPC, tente novamente");

        // Ação
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarLocacao() {

        // Cenário
        Locacao locacao = umLocacao().agora();

        // Ação
        locacaoService.prorrogarLocacao(locacao, 3);

        // Verificação
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class); // Instância do ArgumentCaptor
        Mockito.verify(dao).salvar(argCapt.capture()); // Pede para capturar o que foi passado para o dao no método salvar()
        Locacao locacaoRetornada = argCapt.getValue(); // Essa locacao fica igual à que foi passada para o dao no método salvar()

        error.checkThat(locacaoRetornada.getValor(), is(30.0));
        error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
    }

    @Test
    public void deveAlugarFilme_SemCalcularValor() throws Exception { // Exemplo de como mockar método privado

        // Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        // Mockando método privado
        PowerMockito.doReturn(1.0).when(locacaoService, "calcularValorLocacao", filmes);

        // Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        // Verificação
        Assert.assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao", filmes); // Verifica se o método privado foi chamado
    }
}
