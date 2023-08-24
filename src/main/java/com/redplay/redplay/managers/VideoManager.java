package com.redplay.redplay.managers;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import com.redplay.redplay.connection.HTTPrequests;
import com.redplay.redplay.entities.Usuario;

/**
 * Compilação de diversos métodos para acessar informações sobre
 * vídeos no YouTube.
 */
public class VideoManager {
    static ArrayList<ArrayList<String>> videosArray;
    static VideoManager videomanager = null; //para a implementação do método singleton

    /**Implementação do método singleton
     * Construtor privado do VideoManager.
     * 
     */
    private VideoManager() {
    }

    /**
     * Método getInstance para a implementação do singleton do videomanager.
     * @return a instância do videomanager ou uma nova instância, caso ainda não tenha sido criada.
     */
    public static VideoManager getInstance(){
        if(videomanager == null) {
            videomanager = new VideoManager();
        }
        return videomanager;
    }

    /**
     * Pega várias estatísticas do vídeo, como duração, thumbnails, titulo
     * descrição, tags, data de publicação, entre outros.
     * 
     * @param IDvideo ID do vídeo no YouTube.
     * @return String com as informações do vídeo ou null (caso ocorra uma
     *         Exception).
     * @throws Exception caso ocorra qualquer problema ao pegar as estatísticas do
     *                   vídeo em questão.
     */
    public String getStatistics(String IDvideo) throws Exception {
        HTTPrequests httpRequest = new HTTPrequests(
                "https://youtube.googleapis.com/youtube/v3/videos?id=" + IDvideo
                        + "&part=snippet&part=contentDetails&part=statistics" +
                        "&key=" + Usuario.getApiKey(),
                "GET", false);

        return httpRequest.doHTTPRequest();
    }

    /**
     * Pega as estatísticas de um vídeo e retorna uma em específico.
     * 
     * @param IDvideo ID do video.
     * @param search   Campo das estatísticas a ser retornado (ex.: title,
     *                description...)
     * @return String com o campo das estatísticas selecionado.
     * @throws Exception Caso ocorra qualquer problema ao pegar uma informaçãop
     *                   específica do vídeo em questão.
     */
    public String getInfo(String IDvideo, String search) throws Exception {
        JSONObject estatisticas = new JSONObject(this.getStatistics(IDvideo));
        return estatisticas.get(search).toString();
    }
  
    /**
     * Método que retorna as informações de um vídeo.
     * Conecta os métodos para obter a id por meio de uma 
     * busca com o nome do video. Obter a ID pelo nome do video. 
     * @param videoName Video que terá seus dados listados.
     * @param yearBefore Parametro que definirá a busca antes desse ano.
     * @param monthBefore Parametro que definirá a busca antes desse mês.
     * @param dayBefore Parametro que definirá a busca antes desse dia.
     * @param yearAfter Parametro que definirá a busca depois desse ano.
     * @param monthAfter Parametro que definirá a busca depois desse mês.
     * @param dayAfter Parametro que definirá a busca depois desse dia.
     * @param safeSearch Parametro que define a busca segura.
     * @param videoDuration Parametro que define a duração dos videos a serem pesquisados. 
     * @return JSON com dados do vídeo.
     * @throws Exception Caso não seja possível listar os dados do vídeo.
     */
    public String listVideoData (String videoName, String yearBefore, String monthBefore, String dayBefore,
        String yearAfter, String monthAfter, String dayAfter, String safeSearch, String videoDuration) throws Exception {
        /* Se os parâmetros para filtrar a busca não forem passados, é setado um parâmetro default de busca. */
        if(yearBefore == ""){
            yearBefore = "1970";
            monthBefore = "01";
            dayBefore = "01";
        }    
        if(yearAfter == ""){
            yearAfter = "1970";
            monthAfter = "01";
            dayAfter = "01";
        }
        if(safeSearch == "") safeSearch = "none";
        if(videoDuration == "") videoDuration = "any";
        HTTPrequests request = new HTTPrequests(
            "https://youtube.googleapis.com/youtube/v3/search?key=" + Usuario.getApiKey()
                    + "&type=video&maxResults=20&part=snippet&" 
                    + "publishedAfter=" + yearAfter + "-" + monthAfter + "-" + dayAfter + "T00%3A00%3A00Z&"
                    + "publishedBefore=" + yearBefore + "-" + monthBefore + "-" + dayBefore + "T00%3A00%3A00Z&"
                    + "safeSearch=" + safeSearch
                    + "&videoDuration=" + videoDuration 
                    + "&q=" + URLEncoder.encode(videoName, "UTF-8"),
                "GET", false);
        String response = request.doHTTPRequest();
        return response;
    }

    /** 
     * Método que retorna as informações de uma playlist.
     * Conecta os métodos para obter a id por meio de uma 
     * busca com o nome da Playlist. Obter a ID pelo nome da Playlist.
     * 
     * @param playlistName Playlist pública que terá seus dados listados.
     * @return JSON com dados da playlist.
     * @throws Exception caso não seja possível llistar os dados de uma playlist.
     */
    public String listPlaylistData (String playlistName) throws Exception {
        HTTPrequests request = new HTTPrequests(
            "https://youtube.googleapis.com/youtube/v3/search?key=" + Usuario.getApiKey()
                    + "&type=playlist&maxResults=20&part=snippet&q=" + URLEncoder.encode(playlistName, "UTF-8"),
                "GET", false);

        String response = request.doHTTPRequest();
        return response;
    }

    /**
	 * Lista os videos de um canal através de uma pesquisa.
	 * @param channelID ID do canal a ser buscado.
     * @return JSON com os dados do canal buscado.
	 * @throws Exception Se der qualquer problema com a busca de um canal.
     */
    public String listChannelVideos (String channelID) throws Exception {

        HTTPrequests request = new HTTPrequests(
            "https://youtube.googleapis.com/youtube/v3/search?key=" + Usuario.getApiKey()
                    + "&type=video&maxResults=20&part=snippet&order=date&channelId=" + channelID ,
                "GET", false);
        String response = request.doHTTPRequest();
        return response;
    }

    /**
	 * Busca do canal pelo ID do canal para gerar uma lista de vídeos relacionados a ele.
     * Retorna também as thumbnails e outras estatísticas dos videos, como número
     * de views e número de inscritos.
	 * @param channelID ID do canal a ser buscado.
     * @return As informações do canal buscado (ex.: número de inscritos, thumbnail, etc.).
	 * @throws Exception se der qualquer problema com a busca de um canal.
     */
    public String getChannelInfoById(String channelID) throws Exception{
        HTTPrequests request = new HTTPrequests(
            "https://youtube.googleapis.com/youtube/v3/channels?key="+ Usuario.getApiKey() + "&id=" 
            + channelID + "&part=id&part=snippet&part=statistics", "GET", false);

        String response = request.doHTTPRequest();

        JSONObject infoJson = new JSONObject(response);
        
        return infoJson.toString();
    }

    /**
	 * Busca do canal pelo nome do canal para gerar uma lista de vídeos relacionados a ele.
     * Retorna também as thumbnails e outras estatísticas dos videos, como número
     * de views e número de inscritos.
	 * @param channelName Nome do canal a ser buscado.
     * @return As informações do canal mais relevante com o nome buscado (ex.: número de inscritos, thumbnail, etc.).
	 * @throws Exception Se der qualquer problema com a busca de um canal.
     */
    public String getChannelInfoByName(String channelName) throws Exception{
        HTTPrequests request = new HTTPrequests(
            "https://youtube.googleapis.com/youtube/v3/channels?key="+ Usuario.getApiKey() + "&forUsername=" 
            + URLEncoder.encode(channelName, "UTF-8") + "&part=id&part=snippet&part=statistics", "GET", false);

        String response = request.doHTTPRequest();

        JSONObject infoJson = new JSONObject(response);
        
        return infoJson.toString();
    }
}

