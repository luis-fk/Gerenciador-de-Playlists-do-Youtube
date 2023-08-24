package com.redplay.redplay;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.ObjectManager;
import com.redplay.redplay.managers.VideoManager;
import com.redplay.redplay.site.RedPlayDeleteController;
import com.redplay.redplay.site.RedPlayPostController;

public class DeleteTests {

    private static Usuario usuario;
    /* Cria uma instância do Video Manager comum para tudo */
    static VideoManager videoManager;
    /* Instância auxiliar da playlist */
    static Playlist playlist;
    final static ObjectManager objManager = new ObjectManager();
    private RedPlayDeleteController controller = new RedPlayDeleteController();

    @BeforeEach
    public void setup() {
        usuario = Usuario.getInstance();
        videoManager = VideoManager.getInstance();
        playlist = new Playlist("PlayRed", "");
    }

    @Test
    @Description("Testa o delete de videos de playlist do You Tube")
    void DeleteVideoYouTubeTest() {
        try {
            // adiciona o video
            // Teste de adição de video na playlist no YouTube
            JSONObject data = new JSONObject();
            data.put("playlistID", "PLgZGNwIFLuLru1Od2cEuUEaMsYlRUgJ2B");
            data.put("videoID", "XegysuAKhWg"); 

            RedPlayPostController controller = new RedPlayPostController();
            controller.addVideoYoutube(data.toString());

            // Teste de remoção de video de playlist no YouTube
            JSONObject deletar = new JSONObject();
            deletar.put("IDPlaylist", "PLgZGNwIFLuLru1Od2cEuUEaMsYlRUgJ2B");
            deletar.put("IDvideo", "XegysuAKhWg");

            JSONObject requestBody = new JSONObject();
            requestBody.put("data", deletar);

            RedPlayDeleteController deletecontroller = new RedPlayDeleteController();

            // expected output
            String expectedOutput = "Vídeo removido com sucesso";

            String result = deletecontroller.deletePlaylistVideoYouTube(requestBody.toString());
            assertEquals(expectedOutput, result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Não foi possível deletar o vídeo da playlist do You Tube");
        }
    }
    
    @Test
    @Description("Testa o delete de videos de playlist do Local")
    void DeleteVideoLocalTest() {
        try {
            // Teste de remoção de video de playlist local
            JSONObject requestBody = new JSONObject();
            requestBody.put("indexPlaylist", 0);
            requestBody.put("videoID", "opeETnB8m8w");

            // cria a playlist local
            RedPlayPostController controllerLocal = new RedPlayPostController();
            JSONObject criarPlayLocal = new JSONObject();
            criarPlayLocal.put("playlistName", "PlaylistLocalTest");
            controllerLocal.createPlaylistLocal(criarPlayLocal.toString());

            // adiciona o video
            JSONObject adicionaVideo = new JSONObject();
            adicionaVideo.put("IDVideo", "opeETnB8m8w");
            adicionaVideo.put("index", 0);

            controllerLocal.addVideoPlayListLocal(adicionaVideo.toString());

            String result = controller.deleteLocalPlaylistVideo(requestBody.toString());
            String expectedOutput = "Vídeo removido localmente com sucesso!";
            assertEquals(expectedOutput, result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Não foi possível deletar o vídeo da playlist do You Tube");
        }
    }

}
