package com.redplay.redplay.entities;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;


import com.redplay.redplay.connection.HTTPrequests;

/**
 * Cada instância desta classe (que extende uma entidade genérica) representa
 * o usuário logado no YouTube.
 */
public class Usuario extends Entity<Playlist> {

    /**
     * Instância para a implementação do método singleton
    */
    public static Usuario instance = null;  

    /* Armazena a chave do usuário */
    private static String apiKey = "AIzaSyAXy0l6dTDKjgzmfBoxAN_5osMJR692WNw";

    /* Armazena o ID do usuário */
    private static String IDUser = "UCvQvu2ec6By4w63cpNovk1w";

    /* Armazena uma lista de playlists localmente */
    static ArrayList<Playlist> listaDePlaylistsLocais;

    /**
     * Construtor privado da classe Usuario.
     * 
     * @param nomeDoUsuario Nome do usuário a ser criado, seja o mesmo do YouTube ou
     *                      um nome local.
     */
    private Usuario(String nomeDoUsuario) {
        super(nomeDoUsuario, IDUser);
        listaDePlaylistsLocais = new ArrayList<Playlist>();
    }
    
    /**
     * Getter da instância do usuário. Implementa o método singleton
     * @return Retorna a instância única do Usuário.
     */
    public static Usuario getInstance(){
        if (instance == null) {
            instance = new Usuario("RedPlay"); //determina o nome do usuário
        }
        return instance;
    }

    /**
     * Getter da ApiKey
     * @return A ApiKey
     */
    public static String getApiKey(){
        return apiKey;
    }

    /**
     * Getter do ID do usuário
     * @return O ID do usuário
     */
    public static String getIDUser(){
        return IDUser;
    }
    
    /**
     * Método que cria playlist localmente.
     * @param nomeDaPlaylist String que indica o nome da playlist que será criada.
     * @throws Exception Caso não seja possível criar uma playlist local.
    */
    public void createPlayListLocal(String nomeDaPlaylist) throws Exception{
        int ID = listaDePlaylistsLocais.size();
        Playlist playlistLocal = new Playlist(nomeDaPlaylist, String.valueOf(ID));
        listaDePlaylistsLocais.add(playlistLocal);
    }

    /**
     * Método que adiciona videos em uma playlist local.
     * @param ID Indica o ID do vídeo a ser adicionado na playlist local.
     * @param index Índice da playlist local no ArrayList.
     * @throws Exception Caso não seja possível adicionar um video a uma playlist local.
    */
    public void addVideoPlayListLocal (String ID, int index) throws Exception{
        listaDePlaylistsLocais.get(index).getVideosArray().add(ID);
    }

    /** Remove o video no indice indexVideo da playlist local que está na posição indexPlaylist.
     * @param indexPlaylist Índice da playlist local que o vídeo será removido.
     * @param indexVideo Índice do vídeo no array de vídeos da playlist local.
     * @throws Exception Caso não seja possível remover o vídeo localmente.
     */
    public void removeVideoLocal(int indexPlaylist, int indexVideo) throws Exception{
        listaDePlaylistsLocais.get(indexPlaylist).getVideosArray().remove(indexVideo);
    }

    /**
     * Método que deleta playlist localmente.
     * @param index Recebe o índice da playlist a ser deletada no ArrayList.
     * @throws Exception Caso ocorra algum erro durante a remoção da playlist local.
     */
    public void removePlaylistLocal(int index) throws Exception{
        listaDePlaylistsLocais.remove(index);
    }
    
    /**
     * Listas os dados gerais de todas as playlists do canal. Inclui o título,
     * descrição data de publicação, thumbnail, status de privacidade, id e token
     * da próxima página.
     * @param nextPageToken Token usado para fazer requisições sucessivas para listar
     * todas as páginas dos recursos.
     * @return Retorna o JSON com as informações sobre as playlists do canal
     * @throws Exception caso ocorra qualquer problema ao listar os dados das
     *                   Playlists.
     */
    public String listData(String nextPageToken) throws Exception {
        HTTPrequests request = new HTTPrequests(
                        "https://youtube.googleapis.com/youtube/v3/playlists?pageToken=" + nextPageToken +
                        "&maxResults=50&part=snippet&part=status&channelId=" + ID +
                        "&key=" + Usuario.getApiKey(),
                        "GET", false);
        String response = request.doHTTPRequest();
        return response;
    }

    /**
     * Faz a requisição para criar uma playlist na conta do usuário.
     * 
     * @param title Nome da playlist a ser criada.
     * @throws Exception Caso ocorra qualquer problema ao criar uma playlist.
     * @return JSON com o ID da playlist criada.
     */
    public String requestCreatePlaylist(String title) throws Exception {
        HTTPrequests request = new HTTPrequests(
                "https://youtube.googleapis.com/youtube/v3/playlists?part=snippet&key=" + Usuario.getApiKey(),
                "POST", "{\"snippet\": {\"title\": \"" + title + "\"}}", false);
        String response = request.doHTTPRequest();
        return response;
    }

     /**
     * Requisita à API para remover uma playlist.
     * 
     * @param PlaylistID com a id da playlist a ser removida
     * @throws Exception caso ocorra qualquer problema ao remover uma playlist.
     */
    public void requestToRemovePlaylist(String PlaylistID) throws Exception {
        HTTPrequests request = new HTTPrequests(
                "https://youtube.googleapis.com/youtube/v3/playlists?id=" + PlaylistID +
                        "&key=" + Usuario.getApiKey(),
                "DELETE", false);
        request.doHTTPRequest();
    }

    /**
     * @return Retorna a lista de playlists locais
     */
    public ArrayList<Playlist> getPlayListLocal(){
        return listaDePlaylistsLocais;
    }

    /**
     * Lista os vídeos de determinada playlist local.
     * @param index é a posição da playlist no ArrayList de playlists.
     * @return o JSON com o id dos videos.
     * @throws Exception caso não seja possível listar os vídeos da playlist local.
     */
    public String listPlaylistVideosLocal(int index) throws Exception{
        JSONArray playListArray = new JSONArray();
        for(int i = 0; i < this.getPlayListLocal().get(index).getVideosArray().size(); i++){
            JSONObject body = new JSONObject();
            body.put("VideoID", this.getPlayListLocal().get(index).getVideosArray().get(i));
            playListArray.put(body);
        }
        return playListArray.toString();
    }

    /**
     * Lista as playlists locais do usuário.
     * @return o JSON com as informações sobre as playlists locais.
     * @throws Exception caso não seja possível fazer a requisição.
     */
    public String listPlayListLocalUser() throws Exception{
        JSONArray playListArray = new JSONArray();
        
        /* Para cada playlist local. É criado um JSON com as estatísticas de todos os vídeos dessa playlist local */
        for(int i = 0; i < this.getPlayListLocal().size(); i++){
            JSONObject body = new JSONObject();
            JSONArray IDs = new JSONArray();
            
            for(int j = 0; j < this.getPlayListLocal().get(i).getVideosArray().size(); j++){
                String idVideo = this.getPlayListLocal().get(i).getVideosArray().get(j);
                JSONObject statisticsVideo = new JSONObject();
                statisticsVideo.put("id", idVideo);
                IDs.put(idVideo);
                body.put("nome", this.getPlayListLocal().get(i).getNome());
                body.put("videos", IDs);
                body.put("indice", String.valueOf(i));
                body.put("autor", this.getNome());
                playListArray.put(body);
            }
        }
        return playListArray.toString();
    }
}
