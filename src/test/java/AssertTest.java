import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {

        // Devemos utilizar o mínimo de negações (!) possível

        // Passa no teste se a expressão for verdadeira
        Assert.assertTrue(true);
        // Passa no teste se a expressão for falsa
        Assert.assertFalse(false);

        // Passa no teste se os valores forem iguais
        Assert.assertEquals(1, 1);
        // Para double e float, precisa ter um delta de comparação ("margem de erro")
        Assert.assertEquals(0.51, 0.51, 0.01);
        // Verificar se as strings são iguais
        Assert.assertEquals("bola", "bola");
        // Para ignorar letras maiúsculas e minúsculas ou para comparar apenas o radical, por exemplo, precisa usar o assertTrue com métodos específicos da classe String
        Assert.assertTrue("Bola".equalsIgnoreCase("bola"));
        Assert.assertTrue("bola".startsWith("bo"));
        // Verificar se as strings são diferentes
        Assert.assertNotEquals("bola", "casa");

        // Classes Wrappers - representação em forma de objeto dos tipos primitivos
        // O assertEquals() não compara o tipo primitivo com a sua classe Wrapper
        int i = 5;
        Integer i2 = 5;
        // É preciso converter o tipo primitivo em classe Wrapper
        Assert.assertEquals(Integer.valueOf(i), i2);
        // Ou converter a classe Wrapper em tipo primitivo
        Assert.assertEquals(i, i2.intValue());

        // Para comparação de objetos, é preciso usar o equals do próprio objeto. Precisa implementar o equals na classe
        Usuario u1 = new Usuario("Usuário 1");
        Usuario u2 = new Usuario("Usuário 1");
        Usuario u3 = u2; // Aponta para a mesma instância de u2
        Usuario u4 = null;
        Assert.assertEquals(u1, u2); // Se a classe não tiver o equals implementado, compara os ponteiros e retorna falso
        // Comparar os ponteiros quando a classe tem o equals implementado
        Assert.assertSame(u3, u2);
        // Compara se os ponteiros são diferentes
        Assert.assertNotSame(u1, u2);
        // Verificar se o objeto está nulo
        Assert.assertTrue(u4 == null);
        Assert.assertNull(u4);
        // Verificar se o objeto não está vazio
        Assert.assertNotNull(u3);

        // É possível passar por parâmetro uma String para aparecer como mensagem de erro
        Assert.assertEquals("Erro de comparação:", 1, 1);

    }
}
