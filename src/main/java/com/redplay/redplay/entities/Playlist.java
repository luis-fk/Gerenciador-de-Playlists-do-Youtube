package com.redplay.redplay.entities;

import java.util.ArrayList;

import com.redplay.redplay.connection.HTTPrequests;
/**
 * Cada instância desta classe (que extende uma entidade genérica) representa
 * uma playlist do Youtube.
 */
public class Playlist extends Entity<ArrayList<String>> {

        /* Array que guarda as playlists localmente */
        private ArrayList<String> videosArray;

        /**
         * Construtor da classe Playlist.
         * 
         * @param nomeDaPlaylist Nome da playlist no YouTube ou outro nome desejado.
         * @param IDPlaylist     ID da playlist, obrigatoriamente o mesmo do YouTube.
         */

        public Playlist(String nomeDaPlaylist, String IDPlaylist) {
                super(nomeDaPlaylist, IDPlaylist);
                videosArray = new ArrayList<String>();
        }

        /**
         * Envia uma requisição para remover um vídeo da playlist. 
         * 
         * @param ID ID do vídeo a ser colocado na requisição para ser removido a playlist.
         * @throws Exception Caso ocorra qualquer problema ao tentar remover um vídeo.
         */
         public void requestToRemoveVideo(String ID) throws Exception {
                HTTPrequests httpRequest = new HTTPrequests(
                                "https://youtube.googleapis.com/youtube/v3/playlistItems?id=" + ID
                                                + "&key=" + Usuario.getApiKey(),
                                "DELETE", false);
                httpRequest.doHTTPRequest();
        }

        /**
         * Envia uma requisição para adicionar um vídeo à conta do usuário logada.
         * 
         * @param IDVideo Com a id do video a ser adicionado.
         * @throws Exception Caso ocorra qualquer problema ao adicionar um vídeo a uma
         *                   playlist.
         */
        public void requestToAddVideo(String IDVideo) throws Exception {
                HTTPrequests httpRequest = new HTTPrequests(
                                "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&key=" + Usuario.getApiKey(),
                                "POST", "{\"snippet\": {\"playlistId\": \"" + ID +
                                                "\",\"resourceId\": {\"kind\": \"youtube#video\",\"videoId\": \""
                                                + IDVideo +
                                                "\"}}}",
                                false);
                httpRequest.doHTTPRequest();
        }

        /**
         * Listas os dados gerais de uma playlist. Inclui o título, descrição
         * dono do video (canal), data de publicação e thumbnail.
         * 
         * @param nextPageToken Token usado para fazer requisições sucessivas para listar
         * todas as páginas dos recursos.
         * @return Retorna um JSON que contém os títulos, as descrições,
         *         os autores, a data de publicação
         *         e a URL da thumbnail de todos os vídeos da playlist.
         * @throws Exception Caso ocorra qualquer problema ao listar os dados da
         *                   Playlist.
         */
        public String listData(String nextPageToken) throws Exception {
                HTTPrequests request = new HTTPrequests(
                                "https://youtube.googleapis.com/youtube/v3/playlistItems?pageToken=" + nextPageToken + 
                                "&maxResults=50&part=snippet&playlistId=" + ID + 
                                "&key=" + Usuario.getApiKey(),
                                "GET", false);

                String response = request.doHTTPRequest();
                return response;
        }

        /**
	* Lista os videos de uma determinada playlist.
        * @return Retorna o JSON com as informações sobre os videos listados
        * @throws Exception Retorna uma excessão caso não seja possível selecionar o vídeo
	*/
	public String listPlaylistVideos () throws Exception{
                HTTPrequests request = new HTTPrequests(
                "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&part=id&maxResults=50&playlistId=" 
                        + ID + "&key=" + Usuario.getApiKey(), "GET", false);
                String response = request.doHTTPRequest();
                return response;
        }

        /**
         * Getter do ArrayList de vídeos locais
         * @return Retorna um ArrayList com todos os videos que pertencem a determinada PlayList 
         */
        public ArrayList<String> getVideosArray() {
                return videosArray;
        }
}