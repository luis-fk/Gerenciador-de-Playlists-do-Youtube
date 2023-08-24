package com.redplay.redplay.site;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.VideoManager;

/**
 * Classe que faz a conexão entre o back-end e o front-end. 
 * Esta classe possui métodos relacionados a pegar informações (get), por exemplo,
 * listar as playlists, listar vídeos, etc.
 * Estes métodos não requerem um body em sua chamada, portanto, o Mapping feito pelo
 * Springboot é do tipo Get.
 * Possui a função de ser um controle entre as requisições feitas pelo front-end
 * e o retorno dado pelo back-end.
 */
@RestController
@RequestMapping("/redplay")
@CrossOrigin(origins="*")
public class RedPlayGetController {

    /*Instancia o Usuario que fará as funções da API */
    private static Usuario usuario = Usuario.getInstance();

	/* Cria uma instância do Video Manager comum para tudo */
	static VideoManager videoManager = VideoManager.getInstance();

    /* Instância auxiliar da playlist */
	static Playlist playlist = new Playlist("PlayRed", "");

    /* -------------------------- */
    /* ---REQUESIÇÕES TIPO GET--- */
    /* -------------------------- */
    /**
     * Lista as playlists locais do usuário, bem como os vídeos nessa playlist local.
     * @return string com as playlists locais.
     */
    @GetMapping(path = "/listPlayListLocal", produces="application/json")
    public String listPlayListLocal(){
        try {
            JSONArray playListsLocal = new JSONArray(); // JSON a ser retornado com todas as playlists locais + vídeos das playlists locais
            JSONObject playlistUsuarioLocal = new JSONObject(); // JSON auxiliar com as playlists locais do usuário
            playlistUsuarioLocal.put("local", usuario.getPlayListLocal());

            for(int i = 0; i < playlistUsuarioLocal.getJSONArray("local").length(); i++){
                String nomePlayList = playlistUsuarioLocal.getJSONArray("local").getJSONObject(i).get("nome").toString(); 
                int numeroDeVideos = playlistUsuarioLocal.getJSONArray("local").getJSONObject(i).getJSONArray("videosArray").length();

                //Coloca as informações dos videos em estatisticaVideos
                JSONArray estatisticaVideos = new JSONArray();
                for(int j = 0; j < numeroDeVideos; j++){
                    JSONArray videos = playlistUsuarioLocal.getJSONArray("local").getJSONObject(i).getJSONArray("videosArray");
                    String estatisticas = videoManager.getStatistics(videos.getString(j));
                    estatisticaVideos.put(new JSONObject(estatisticas));
                }
                JSONObject playlist = new JSONObject();
                playlist.put("nomePlayList", nomePlayList);
                playlist.put("numeroDeVideos", String.valueOf(numeroDeVideos));
                playlist.put("videos", estatisticaVideos);
                playlist.put("autor", usuario.getNome());
                playListsLocal.put(playlist);
            }
            
            return playListsLocal.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar as playlists localmente!";
        }
    }
    
    /**
     * Lista as playlists do usuário no youtube.
     * @return string com as playlists do youtube.
     */
    @GetMapping(path = "/listPlayListYouTube", produces="application/json")
    public String listPlayListYouTube(){
        try {
            return usuario.listData("");
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar as playlists!";
        }
    }

    /**
     * Lista todos os vídeos de todas as playlists, tanto locais quanto no youtube do usuário.
     * @return JSON de todos os vídeos do usuário.
     */
    @GetMapping(path = "/listAllVideosAllPlaylists", produces = "application/json")
    public @ResponseBody String listAllVideosAllPlaylists(){
        try{
            String playlistYouTube = usuario.listData("");
            JSONObject playListYT = new JSONObject(playlistYouTube);

            ArrayList<String> idPlayLists = new ArrayList<String>();

            // Percorre as playlists
            for(int i = 0; i < playListYT.getJSONArray("items").length(); i++){
                String idPlaylist = playListYT.getJSONArray("items").getJSONObject(i).get("id").toString();
                // Adiciona o id da playlist
                idPlayLists.add(idPlaylist.toString());
            }

            JSONObject estatisticas = new JSONObject(); // JSON auxiliar que fará uma requisição para retornar as estatísticas de cada vídeo
            int index = 0;
            // Percorre o vetor de IDs de playlists e em cada playlists, 
            // Percorre o vetor de videos e pega as informações de cada um e coloca em um JSONArray
            for(int i = 0; i < idPlayLists.size(); i++){
                playlist.setID(idPlayLists.get(i)); //Seta o ID da playlist a ser realizada a busca
                String infoPlayList = playlist.listPlaylistVideos();

                JSONObject data = new JSONObject(infoPlayList); // JSON com os dados a serem retornados
                String IDvideo; // String auxiliar com o ID do vídeo

                /*
                * Faz uma requisição adicional, para cada vídeo da playlist, pelas estatísticas
                * do vídeo e põe em um JSON auxiliar
                */
                for (int j = 0; j < data.getJSONArray("items").length(); j++) {
                    IDvideo = data.getJSONArray("items").getJSONObject(j).getJSONObject("snippet")
                            .getJSONObject("resourceId").get("videoId").toString();
                    estatisticas.put(String.valueOf(index), new JSONObject(videoManager.getStatistics(IDvideo)));
                    index++;
                }
            }
            // Pega as informações das playlists locais e coloca no JSON estatisticas
            for(int i = 0; i < usuario.getPlayListLocal().size(); i++){
                for(int j = 0; j < usuario.getPlayListLocal().get(i).getVideosArray().size(); j++){
                    String IDvideo = usuario.getPlayListLocal().get(i).getVideosArray().get(j);
                    estatisticas.put(String.valueOf(index), new JSONObject(videoManager.getStatistics(IDvideo)));
                    index++;
                }
            }
            return estatisticas.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "Não foi possível listar todos os vídeos de todas as playlists";
        }
    }
}
