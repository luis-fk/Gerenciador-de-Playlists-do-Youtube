package com.redplay.redplay.site;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.VideoManager;

/**
 * Classe que faz a conexão entre o back-end e o front-end.
 * Esta classe possui os métodos relacioados à requisições de deletar algo
 * (deletar
 * vídeo, deletar playlist, etc.)
 * Possui a função de ser um controle entre as requisições feitas pelo front-end
 * e o retorno dado pelo back-end.
 * É importante notar que ainda que sejam requisições do tipo Delete, o
 * Mapping feito pelo Springboot é do tipo Post, pois os métodos exigem um body
 * em sua chamada.
 */
@RestController
@RequestMapping("/redplay")
@CrossOrigin(origins = "*")
public class RedPlayDeleteController {

    /* Instancia o Usuario que fará as funções da API */
    private static Usuario usuario = Usuario.getInstance();

    /* Cria uma instância do Video Manager comum para tudo */
    static VideoManager videoManager = VideoManager.getInstance();

    /* Instância auxiliar da playlist */
    static Playlist playlist = new Playlist("PlayRed", "");

    /* --------------------------- */
    /* --REQUESIÇÕES TIPO DELETE-- */
    /* --------------------------- */

    /**
     * Deleta uma playlist local.
     * 
     * @param body Com o índice da playlist que deseja ser deletada.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     * 
     */
    @PostMapping(path = "/deletePlaylistLocal", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteLocalPlaylist(@RequestBody String body) {
        try {
            JSONObject localPlaylistIndex = new JSONObject(body);
            usuario.removePlaylistLocal(localPlaylistIndex.getInt("index"));
            return "Playlist local removida com sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Deleta uma playlist na conta do usuário.
     * 
     * @param body Com o ID da playlist a ser excluida.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/deletePlaylistYouTube", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deletePlaylist(@RequestBody String body) {
        try {
            JSONObject playlistName = new JSONObject(body);
            usuario.requestToRemovePlaylist(playlistName.get("playlistID").toString());
            return "Playlist excluida com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível excluir a playlist!";
        }
    }

    /**
     * Remove um video de uma playlist local.
     * Este método recebe o ID do vídeo a ser removido na playlist local. Realiza uma busca neste playlist
     * para encontrar o índice do vídeo no ArrayList e, em seguida, remove o vídeo neste índice.
     * 
     * @param body com o índice da playlist a ser excluida e o ID a ser removido na playlist local.
     * @return mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(path = "/deleteLocalPlaylistVideo", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteLocalPlaylistVideo(@RequestBody String body) {
        try {
            JSONObject info = new JSONObject(body);
            int indexPlaylist = info.getJSONObject("data").getInt("indexPlaylist");
            String IDvideo = info.getJSONObject("data").getString("videoID");

            for (int i = 0; i < usuario.getPlayListLocal().size(); i++) {
                if (IDvideo.equals(usuario.getPlayListLocal().get(indexPlaylist).getVideosArray().get(i))) {
                    usuario.removeVideoLocal(indexPlaylist, i);
                    return "Vídeo removido localmente com sucesso!";
                }
            }
            return "Não foi possível fazer a requisição, pois o video nao foi encontrado!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível fazer a requisição!";
        }
    }

    /**
     * Remove um video de uma playlist na conta do usuário.
     * Importante notar que o ID do vídeo no youtube é diferente do ID do vídeo
     * dentro da playlist.
     * Este método receberá o ID do vídeo no youtube e em seguida encontrará o ID do
     * vídeo na
     * playlist e, por fim, deletará este vídeo.
     * 
     * @param body com o ID do video a ser excluido e o ID da playlist que deseja se
     *             deletar o vídeo.
     * @return mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/deletePlaylistVideoYouTube", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deletePlaylistVideoYouTube(@RequestBody String body) {
        try {
            JSONObject infoID = new JSONObject(body);

            JSONObject data = infoID.getJSONObject("data");
            String idPlayList = data.getString("IDPlaylist");

            String idVideo = data.getString("IDvideo");

            playlist.setID(idPlayList);

            JSONObject videosPlayList = new JSONObject(playlist.listPlaylistVideos()); // Lista todos os vídeos da playlist e põe em um JSON auxiliar

            /*
             * Faz uma busca no JSON de videos da playlist. Para cada elemento, realiza uma
             * comparação com ID e verifica se os IDs são iguais.
             */
            for (int i = 0; i < videosPlayList.getJSONArray("items").length(); i++) {
                String aux = videosPlayList.getJSONArray("items").getJSONObject(i).getJSONObject("snippet")
                        .getJSONObject("resourceId").get("videoId").toString();
                if (aux.equals(idVideo)) {
                    System.out.println("O ID E: " + aux);
                    playlist.requestToRemoveVideo(
                            videosPlayList.getJSONArray("items").getJSONObject(i).get("id").toString()); // Remove o video com o ID desejado
                    return "Vídeo removido com sucesso";
                }
            }
            return "Não foi possível achar o vídeo na playlist!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível deletar o video da playlist!";
        }
    }
}
