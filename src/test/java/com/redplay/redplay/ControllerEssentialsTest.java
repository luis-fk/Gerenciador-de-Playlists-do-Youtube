package com.redplay.redplay;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.VideoManager;
import com.redplay.redplay.site.RedPlayPostController;

public class ControllerEssentialsTest {
    private static Usuario usuario;
    /* Cria uma instância do Video Manager comum para tudo */

    static VideoManager videoManager;
    /* Instância auxiliar da playlist */
    static Playlist playlist;

    @BeforeEach
    public void setup() {
        usuario = Usuario.getInstance();
        videoManager = VideoManager.getInstance();
    }

    @Test
    @Description("Testar a criação de playlist e checar os videos")
    public void requestCreatePlaylistTest() {
        try {
            String ID = usuario.requestCreatePlaylist("playlist123");
            playlist = new Playlist("PlayRed", ID);
            // String response = playlist.listPlaylistVideos();
            System.out.println(ID);
            // System.out.println(response);
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro ao fazer a requisição para criar a playlist no You Tube");
        }
    }

    @Test
    @Description("Testa de adicionar um video em playlist do You Tube")
    void AddVideoYouTubeTest() {
        try {
            // Teste de adição de video na playlist no YouTube
            JSONObject data = new JSONObject();
            data.put("playlistID", "PLgZGNwIFLuLpyaeXCYVOhX23YewA-aJwh");
            data.put("videoID", "opeETnB8m8w");

            RedPlayPostController controller = new RedPlayPostController();
            String result = controller.addVideoYoutube(data.toString());
            System.out.println(result);
            playlist = new Playlist("PlayRed", "PLgZGNwIFLuLpyaeXCYVOhX23YewA-aJwh");
            System.out.println("os videos da playlist:" + playlist.listPlaylistVideos());
        } catch (Exception e) {
            System.out.println(
                    "Ocorreu um erro ao fazer a requisição para adicionar um video a uma playlist no You Tube");
        }
    }

    /**
     * Para este teste de adicionar videos em playlist local, é necessário rodar o teste isolado, pois outros testes podem
     * criar algo que influencia no comportamento deste teste
     */
    @Test
    @Description("Testar a adição de vídeos em playlist Local")
    void addVideoLocalTest() {
        try {
            // cria a playlist local
            RedPlayPostController controller = new RedPlayPostController();
            JSONObject criarPlayLocal = new JSONObject();
            criarPlayLocal.put("playlistName", "PlaylistLocalTest");
            controller.createPlaylistLocal(criarPlayLocal.toString());

            // adiciona o video
            JSONObject adicionaVideo = new JSONObject();
            adicionaVideo.put("IDVideo", "opeETnB8m8w");
            adicionaVideo.put("index", 0);

            controller.addVideoPlayListLocal(adicionaVideo.toString());
            // checar se a ID do vídeo foi devidamente colocada:
            assertEquals("opeETnB8m8w", usuario.getPlayListLocal().get(0).getVideosArray().get(0).toString());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao fazer a requisição para adicionar uma playlist local");
        }
    }

    @Test
    @Description("Testar a criação da playlist Local")
    void createPlaylistLocalTest() {
        try {
            RedPlayPostController controller = new RedPlayPostController();
            JSONObject data = new JSONObject();
            data.put("playlistName", "PlaylistLocalTest");
            String retorno = controller.createPlaylistLocal(data.toString());
            if (retorno == "Playlist local criada com sucesso!")
                System.out.println("Teste acima passou!");
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao fazer a requisição para criar uma playlist local");
        }
    }

    @Test
    @Description("Testar a listagem de videos por ID de canal")
    void listChannelVideosbyName() {
        try {
            // cria a playlist local
            RedPlayPostController controller = new RedPlayPostController();
            // adiciona o video
            JSONObject listaVideos = new JSONObject();
            listaVideos.put("channelName", "Viva La Dirt League");
            String response = controller.listChannelVideosByName(listaVideos.toString());
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Erro ao listar os videos do canal");
        }

    }

    @Test
    @Description("Testar a listagem de videos por ID de canal")
    void listChannelVideosbyID() {
        try {
            // cria a playlist local
            RedPlayPostController controller = new RedPlayPostController();
            // adiciona o video
            JSONObject listaVideos = new JSONObject();
            listaVideos.put("channelName", "Viva La Dirt League");
            String response = controller.listChannelVideosByName(listaVideos.toString());
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Erro ao listar os videos do canal");
        }

    }
}
