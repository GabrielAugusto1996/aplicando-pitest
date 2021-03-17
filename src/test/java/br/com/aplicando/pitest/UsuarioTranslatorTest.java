package br.com.aplicando.pitest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsuarioTranslatorTest {

    @Test
    @DisplayName("Deverá retornar Usuario - Quando sucesso")
    void of_Usuario_QuandoSucesso() {
        final Integer idade = 10;
        final String nome = "Dudu";

        final Usuario usuario = UsuarioTranslator.of(nome, idade);

        Assertions.assertNotNull(usuario);
        Assertions.assertEquals(nome, usuario.getNome());
        Assertions.assertEquals(idade, usuario.getIdade());
    }
}