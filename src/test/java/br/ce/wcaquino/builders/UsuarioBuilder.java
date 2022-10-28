package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
        // O construtor fica privado para que não seja possível criar instâncias do Builder externamente ao próprio Builder
    }

    public static UsuarioBuilder umUsuario() { // Público e estático para que seja acessado externamente sem a necessidade de uma instância
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuario 1");
        return builder; // Retornando a instância do builder, posso chamar outros métodos do próprio builder - Chaining Method
    }

    public UsuarioBuilder comNome(String nome) {
        usuario.setNome(nome);
        return this;
    }

    public Usuario agora() {
        return usuario;
    }
}
