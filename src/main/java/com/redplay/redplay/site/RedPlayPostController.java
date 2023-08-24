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
 * Esta classe possui métodos relacionados ao tipo Post, ou seja, adicionar
 * vídeos,
 * adicionar playlists, etc. Além disso, é importante notar que os métodos do
 * tipo Post
 * exigem um body em sua chamada.
 * Possui a função de ser um controle entre as requisições feitas pelo front-end
 * e o retorno dado pelo back-end.
 */
@RestController
@RequestMapping("/redplay")
@CrossOrigin(origins = "*")
public class RedPlayPostController {

    /* Instancia o Usuario que fará as funções da API */
    private static Usuario usuario = Usuario.getInstance();

    /* Cria uma instância do Video Manager comum para tudo */
    static VideoManager videoManager = VideoManager.getInstance();

    /* Instância auxiliar da playlist */
    static Playlist playlist = new Playlist("PlayRed", "");

    /* --------------------------- */
    /* ---REQUESIÇÕES TIPO POST--- */
    /* --------------------------- */

    /**
     * Pega o nome do vídeo que o usuário deseja procurar e retorna um JSON
     * com as informações de todos os videos relacionados a esse título.
     * Ao buscar um vídeo, é possível realizar um filtro de busca com diversos parâmetros.
     * Ex.: data de publicação, duração, restrição de idade, etc.
     * 
     * @param body Com o nome do video que o usuário deseja procurar e com os parâmetros de busca desejados.
     * @return Informações de todos os videos relacionados a essa busca.
     */
    @PostMapping(path = "/searchVideo", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String searchVideo(@RequestBody String body) {
        try {
            JSONObject infoSearch = (new JSONObject(body));
            infoSearch = infoSearch.getJSONObject("data");
            // Retorna os videos da pesquisa
            return videoManager.listVideoData(infoSearch.getString("videoName"), infoSearch.getString("yearBefore"),
                    infoSearch.getString("monthBefore"), infoSearch.getString("dayBefore"),
                    infoSearch.getString("yearAfter"), infoSearch.getString("monthAfter"),
                    infoSearch.getString("dayAfter"), infoSearch.getString("safeSearch"),
                    infoSearch.getString("videoDuration"));

        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível buscar o video!";
        }
    }

    /**
     * Pega o nome da playlist que o usuário deseja procurar e retorna um JSON
     * com as informações de todos os videos relacionados a playlist com este título.
     * 
     * @param body Com o nome da playlist que o usuário deseja procurar.
     * @return Informações de todas as playlists relacionadas a essa busca.
     */
    @PostMapping(path = "/searchPlaylist", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String searchPlaylist(@RequestBody String body) {
        try {
            JSONObject playlistName = new JSONObject(body);
            return videoManager.listPlaylistData(playlistName.get("playlistName").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível buscar a playlist!";
        }
    }

    /**
     * Pega as informações de determinado video de acordo com seu ID.
     * 
     * @param body com o id do video a ser pego as estatísticas.
     * @return estatisticas sobre o video.
     */
    @PostMapping(path = "/infovideo", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getInfoVideo(@RequestBody String body) {
        try {
            JSONObject IDbody = new JSONObject(body);
            //JSON auxiliar com as estatísticas do vídeo
            JSONObject videoStatistics = new JSONObject(videoManager.getStatistics(IDbody.get("IDVideo").toString()));
            //JSON auxliar com as informações do canal que publicou este vídeo
            JSONObject infoDoCanal = new JSONObject(
                videoManager.getChannelInfoById(videoStatistics.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").get("channelId").toString())
            );
            //JSON que será retornado com as estatísticas do vídeo + estatísticas do canal que publicou o vídeo
            JSONObject video = new JSONObject();
            video.put("videoStatistics", videoStatistics);
            video.put("channelStatistics", infoDoCanal);
            return video.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível pegar as informações do video!";
        }
    }

    /**
     * Lista todos os vídeos de um determinado canal do youtube, a busca é realizada
     * pelo NOME.
     * 
     * @param body com o nome do canal dos videos a serem procurados.
     * @return info dos videos procurados.
     */
    @PostMapping(path = "/listChannelVideosByName", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String listChannelVideosByName(@RequestBody String body) {

        try {
            JSONObject bodyJson = new JSONObject(body);

            JSONObject infoDoCanal = new JSONObject();

            // Retorna as estatisticas do canal
            infoDoCanal.put("estatisticas",
                    new JSONObject(videoManager.getChannelInfoByName(bodyJson.get("channelName").toString())));

            // Pega o id do canal
            String IDcanal = infoDoCanal.getJSONObject("estatisticas").getJSONArray("items").getJSONObject(0).get("id")
                    .toString();

            JSONObject infoVideosDoCanal = new JSONObject(); // JSON auxiliar com as informações dos vídeos deste canal

            // Lista os videos do canal pelo ID do canal
            infoVideosDoCanal.put("videos", new JSONObject(videoManager.listChannelVideos(IDcanal)));

            // Adiciona no JSON infoVideosDoCanal informações sobre o canal (statistics) no parametro statisticsChannel
            infoVideosDoCanal.put("statisticsChannel",
                    infoDoCanal.getJSONObject("estatisticas").getJSONArray("items").getJSONObject(0).getJSONObject("statistics"));

            // Adiciona no JSON infoVideosDoCanal informações sobre o canal (snippet) no parametro statisticsChannel
            infoVideosDoCanal.getJSONObject("statisticsChannel").put("snippet", infoDoCanal.getJSONObject("estatisticas").getJSONArray("items").
                                getJSONObject(0).getJSONObject("snippet"));

            return infoVideosDoCanal.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar os videos de um canal pelo nome do canal!";
        }
    }

    /**
     * Lista todos os vídeos de um determinado canal do youtube, a busca é realizada
     * pelo ID do canal.
     * 
     * @param body com o ID do canal dos videos a serem procurados.
     * @return info dos videos procurados.
     */
    @PostMapping(path = "/listChannelVideosByID", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String listChannelVideosByID(@RequestBody String body) {
        JSONObject bodyJson = new JSONObject(body);
        try {
            return videoManager.listChannelVideos(bodyJson.get("channelID").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar os videos de um canal pelo ID do canal!";
        }
    }

    /**
     * Lista todos os vídeos de determinada playlist local, a busca é realizada pelo
     * indice da playlist no ArrayList de playlists.
     * 
     * @param body Com o índice local da playlist a ser listada.
     * @return JSON dos videos da playlist.
     */
    @PostMapping(path = "/listPlaylistVideosLocal", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String listPlaylistVideosLocal(@RequestBody String body) {
        JSONObject bodyJson = new JSONObject(body);
        try {
            return usuario.listPlaylistVideosLocal(bodyJson.getInt("index"));
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar os video de uma playlist local!";
        }
    }

    /**
     * Lista todos os vídeos de determinada playlist no youtube, a busca é realizada
     * pelo ID da playlist.
     * 
     * @param body Com o ID da playlist a ser listada.
     * @return JSON dos videos da playlist.
     */
    @PostMapping(path = "/listPlaylistVideosYouTube", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String listPlaylistVideosYouTube(@RequestBody String body) {
        JSONObject bodyJson = new JSONObject(body);
        try {
            playlist.setID(bodyJson.getString("IDPlayList")); // Seta o ID da playlist a ser realizada a busca
            JSONObject data = new JSONObject(playlist.listPlaylistVideos()); // JSON com os dados a serem retornados
            String IDvideo; // String auxiliar com o ID do vídeo
            JSONObject estatisticas = new JSONObject(); // JSON auxiliar que fará uma requisição para retornar as estatísticas de cada vídeo

            /*
             * Faz uma requisição adicional, para cada vídeo da playlist, pelas estatísticas
             * do vídeo e põe em um JSON auxiliar
             */
            for (int i = 0; i < data.getJSONArray("items").length(); i++) {
                IDvideo = data.getJSONArray("items").getJSONObject(i).getJSONObject("snippet")
                        .getJSONObject("resourceId").get("videoId").toString();
                estatisticas.put(String.valueOf(i), new JSONObject(videoManager.getStatistics(IDvideo)));
            }
            data.append("estatisticas", estatisticas); // Junta o JSON das estatísticas com o JSON dos vídeos listados
            return data.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível listar os videos de uma playlist!";
        }
    }

    /**
     * Cria uma playlist local.
     * 
     * @param body Com o nome da playlist.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/createPlaylistLocal", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String createPlaylistLocal(@RequestBody String body) {
        try {
            JSONObject playlistName = new JSONObject(body);
            usuario.createPlayListLocal(playlistName.get("playlistName").toString());
            return "Playlist local criada com sucesso!";
        } catch (Exception e) {
            return "Não foi possível fazer a requisição!";
        }
    }

    /**
     * Cria uma playlist no youtube na conta do usuário.
     * 
     * @param body Com o nome da playlist a ser criada.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/createPlaylistYouTube", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String createPlaylistYouTube(@RequestBody String body) {
        try {
            JSONObject playlistName = new JSONObject(body);
            String info = usuario.requestCreatePlaylist(playlistName.get("playlistName").toString());
            JSONObject playlistID = new JSONObject(info);

            return playlistID.getString("id");

        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível criar uma playlist!";
        }
    }

    /**
     * Adiciona um video em uma playlist local. A requisição é feita a partir do ID
     * do vídeo a ser adicionado.
     * 
     * @param body Com o ID do video a ser adicionado, juntamente com o indice da
     *             playlist.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/addVideoPlaylistLocal", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String addVideoPlayListLocal(@RequestBody String body) {
        try {
            JSONObject playlistName = new JSONObject(body);
            usuario.addVideoPlayListLocal(playlistName.get("IDVideo").toString(), playlistName.getInt("index"));
            return "Vídeo adicionado localmente com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível criar uma playlist!";
        }
    }

    /**
     * Adiciona um vídeo numa playlist na conta do usuário no youtube.
     * 
     * @param body Com o ID da playlist e o ID do video.
     * @return Mensagem se o requerimento foi um sucesso ou não.
     */
    @PostMapping(value = "/addVideoPlaylistYouTube", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addVideoYoutube(@RequestBody String body) {
        try {
            JSONObject data = new JSONObject(body);
            playlist.setID(data.get("playlistID").toString());
            playlist.requestToAddVideo(data.get("videoID").toString());
            return data.get("videoID").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível adicionar o video!";
        }
    }
}
