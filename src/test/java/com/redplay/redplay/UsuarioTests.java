package com.redplay.redplay;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.ObjectManager;
import com.redplay.redplay.managers.VideoManager;

public class UsuarioTests {
    /* Cria uma instância do Usuário comum para tudo */
    private static Usuario usuario;

    /* Cria uma instância do Video Manager comum para tudo */
    static VideoManager vm;
    
    /* Instância auxiliar da playlist */
    static Playlist playlist;

    @BeforeEach
    public void setup() {
        usuario = Usuario.getInstance();
        vm = VideoManager.getInstance();
    }

    @Test
    @Description("Testar a funcao requestCreatePlaylist")
    public void requestCreatePlaylistTest() {
        try {
            usuario.requestCreatePlaylist("Teste Request Create Playlist");
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro ao fazer a requisição para criar a playlist");
        }
    }

    @Test
    @Description("Testar a funcao listData")
    public void listDataTest() {
        try {
            System.out.println(usuario.listData(""));
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro ao fazer o listData (listar as playlists)");
        }
    }

    @Test
    @Description("Testar a funcao createPlayListLocal")
    public void createPlayListLocalTest() {
        try {
            usuario.createPlayListLocal("Teste criarPlayListLocal");
            assertEquals("Teste criarPlayListLocal", usuario.getPlayListLocal().get(0).getNome());
            System.out.println(usuario.listData(""));
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro ao fazer o createPlayListLocal (criar as playlists locais)");
        }
    }

    @Test
    @Description("Testar a funcao addVideoPlayListLocal")
    public void addVideoPlayListLocalTest() {
        try {
            //cria a playlist local
            usuario.createPlayListLocal("Teste criarPlayListLocal");

            //adiciona um vídeo
            usuario.addVideoPlayListLocal("IDVideoTeste", 0); //adiciona um video com o ID "IDVideoTeste" na playlist de indice 0
            assertEquals("IDVideoTeste", usuario.getPlayListLocal().get(0).getVideosArray().get(0));
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro ao fazer o addVideoPlayListLocal (adicionar video em playlists locais)");
        }
    }


}
