package com.redplay.redplay.connection;


import java.io.IOException;

import org.json.JSONObject;

import com.redplay.redplay.managers.ObjectManager;

/** 
 * Gerencia a autenticação do usuário, ou seja,
 * o acesso a uma conta do YouTube através do CLIENT_SECRET e
 * do REFRESH_TOKEN obtidos como credenciais da API.
 * Utiliza o padrão Singleton.
 */
public class Authentication {
    /*
     * Cria uma instância do JSONManager para ser usada quando é necessário
     * ler o arquivo com as credenciais de acesso do usuário à API
     */
    final static ObjectManager jsonManager = new ObjectManager();
    /* Guarda as credenciais em um arquivo JSON */
    private JSONObject credentials = null;

    /*Instancia para o singleton*/
    static Authentication instance = null;

    /*Construtor privado*/
    private Authentication(){}

    /**
     * Método que retorna a instância da autenticação usando o padrão Singleton.
     * @return A instância da autenticação ou cria uma nova instância, caso ainda não tenha sido criada uma primeira vez.
     */
    public static Authentication getInstance(){
        if(instance == null){
            instance = new Authentication();
        }
        return instance;
    }

    /**
     * Obtém o token de acesso que a API precisa para acessar
     * funções que alteram a conta do usuário logado.
     * 
     * @return String com o token de acesso à API.
     * @throws Exception Caso ocorra qualquer problema ao tentar pegar o token de
     *                   acesso à conta do usuário.
     */
    public String getAccessToken() throws Exception {
        /*
         * Tenta pegar as credenciais. Se não conseguir é uma exception da leitura do
         * arquivo, e isso acontece quando credentials continua como null depois de
         * tentar obtê-las pela função storeCredentials
         */
        if (credentials == null) {
            storeCredentials();
        }
        if (credentials == null) {
            throw new IOException("O arquivo contendo as credenciais não existe.");
        }

        /* Organiza a conexão HTTP */
        HTTPrequests HTTPConnector = new HTTPrequests(
                "https://oauth2.googleapis.com/token", "POST",
                "client_secret=" +
                        credentials.getString("client_secret") +
                        "&grant_type=refresh_token&refresh_token=" +
                        credentials.getString("refresh_token") +
                        "&client_id=" + credentials.getString("client_id"),
                true);

        /* Realiza a conexão HTTP e guarda a resposta em um JSONObject */
        JSONObject response = new JSONObject(HTTPConnector.doHTTPRequest());

        /* Retorna o token de acesso */
        return response.get("access_token").toString();
    }

    /**
     * Guarda as credenciais no atributo credentials.
     * Caso o arquivo client.json não seja encontrado, seta credentials
     * como null. Esse problema será tratado em um escopo superior.
     */
    protected void storeCredentials() {
        try {
            credentials = jsonManager.readFile(
                    "./src/main/java/com/redplay/redplay/resources/client.json");
        } catch (IOException e) {
            credentials = null;
        }
    }
}
