package com.redplay.redplay.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Gerencia tudo o que está relacionado com Requisições HTTP.
 */
public class HTTPrequests {
    /**
     * True se for uma uma requisição de autenticação.
     * False se for uma requisição normal para a API.
     */
    boolean auth;
    /**
     * Instância usada para obter o token de login do usuário.
     */
    final Authentication authenticator = Authentication.getInstance();

    /**
     * Guarda o URL da requisição HTTP.
     */
    private String url;

    /** Indica o tipo da requisição: GET, POST, PUT, DELETE, etc. */
    private String type;

    /**
     * Quarda o corpo da requisição. Se requisição exige body,
     * estará no formato de String. Caso contrário, será null.
     */
    private String body;

    /**
     * Construtor para requisições HTTP sem body.
     * 
     * @param url  URL da requisição a ser feita.
     * @param type Tipo da requisição (GET, POST, PUT, DELETE, etc)
     * @param auth true: requisição é de autenticação (usuário sendo "logado").
     *             false: requisição genérica.
     */
    public HTTPrequests(String url, String type, boolean auth) {
        this.url = url;
        this.type = type;
        this.auth = auth;
        this.body = null;
    };

    /**
     * Construtor para requisições HTTP com body.
     * 
     * @param url         URL da requisição a ser feita.
     * @param type        Tipo da requisição (GET, POST, PUT, DELETE, etc)
     * @param requestBody String com o corpo da requisição
     * @param auth        true: requisição é de autenticação (usuário sendo
     *                    "logado").
     *                    false: requisição genérica.
     */
    public HTTPrequests(String url, String type, String requestBody, boolean auth) {
        this.url = url;
        this.type = type;
        this.auth = auth;
        this.body = requestBody;
    };

    /**
     * 
     * Método que realiza a requisição HTTP e retorna a resposta do servidor.
     * Também se adapta às situações de ser uma request de autenticação
     * ou uma request normal à API já autenticada.
     * 
     * @return String com a resposta do servidor
     * @throws Exception Se ocorrer algum problema com a conexão HTTP de qualquer
     *                   tipo, seja problema de rede ou problema na requisição em
     *                   si.
     */
    public String doHTTPRequest() throws Exception {
        /* Cria a requisição HTTP baseada na URL */
        URL apiURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();

        setHeader(connection, authenticator, type, auth);

        /* Verifica se a requisição tem um corpo. Se tiver, irá criar o body */
        if (body != null) {
            setBody(connection, body);
        }

        /* getResponse */
        return getResponse(connection);
    }

    /**
     * Adiciona o header à requisição HTTP.
     * 
     * @param connection    Conexão HTTP em questão.
     * @param authenticator Instância da Authentication para "logar" o usuário à API
     * @param type          String com o tipo da requisição HTTP.
     * @param auth          true: requisição é de autenticação (usuário sendo
     *                      "logado").
     *                      false: requisição genérica.
     * @throws Exception caso ocorra algum problema ao adicionar o header (o que é
     *                   inesperado).
     */
    private static void setHeader(HttpURLConnection connection,
            Authentication authenticator, String type, boolean auth)
            throws Exception {

        /* Adiciona o tipo da requisição no header da requisição */
        connection.setRequestMethod(type);
        /* Declara que será usado JSON na requisição */
        connection.setRequestProperty("Content-Type", "application/json");

        /* Se for uma requisição de autenticação, configura como tal */
        if (auth) {
            connection.setRequestProperty("Host", "oauth2.googleapis.com");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-agent", "google-oauth-playground");
        }
        /* Caso contrário, configura como uma requisição para a API */
        else {
            /* Coloca o token de acesso no header, obtido da classe Authentication */
            connection.setRequestProperty("Authorization", "Bearer " + authenticator.getAccessToken());
        }
    }

    /**
     * Adiciona o corpo a uma requisição HTTP.
     * 
     * @param connection Requisição HTTP em questão.
     * @param body       Corpo da requisição a ser adicionado.
     * @throws Exception caso ocorra qualquer problema ao adicionar um corpo à
     *                   requisição.
     */
    private static void setBody(HttpURLConnection connection, String body) throws Exception {
        byte[] requestBodyBytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setRequestProperty("Content-length", String.valueOf(requestBodyBytes.length));

        /* Permite que a requisição receba um corpo */
        connection.setDoOutput(true);

        /* Escreve o corpo na requisição por meio de uma stream */
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Envia a requisição HTTP e recebe a resposta do servidor
     * 
     * @param connection A conexão HTTP criada.
     * @return String com a resposta do servidor.
     * @throws Exception se ocorrer qualquer problema ao enviar a requisição ou
     *                   receber a resposta.
     */
    private static String getResponse(HttpURLConnection connection) throws Exception {
        /*
         * Envia a request pelo input da conexão (InputStream)
         * e já lê a resposta (BufferedReader)
         */
        BufferedReader responseReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
                
        /* Recurso para construir uma String com a resposta da API */
        StringBuilder stringBuilder = new StringBuilder();
        /* Guarda cada linha da resposta da API */
        String responseLine;
        /* Lê cada linha da resposta da API e compila numa String */
        while ((responseLine = responseReader.readLine()) != null) {
            stringBuilder.append(responseLine + '\n');
        }
        responseReader.close();
        return stringBuilder.toString();
    }
}
