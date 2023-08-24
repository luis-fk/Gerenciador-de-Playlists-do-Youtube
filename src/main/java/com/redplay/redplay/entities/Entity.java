package com.redplay.redplay.entities;

/**
 * Define os parâmetros com conjunto de uma entidade da API.
 * A exemplo disso, as classes Playlist e Usuário compartilham a mesma estrutura
 * de terem um ID, um nome e usarem um ObjectManager para organizar os retornos
 * de
 * suas funções.
 */
public abstract class Entity<Type> {
    /** ID identificador da entidade. Usado para requisições à API. */
    protected String ID;
    /** Nome da entidade. Ex.: nome do usuário ou playlist. */
    protected String nome;

    /**
     * Construtor da classe Entidade.
     * 
     * @param nome       Nome a ser dado à entidade (ex.: nome do usuário ou
     *                   playlist).
     * @param ID         Identificador da entidade. Usado para requisições à API.
     * @param objManager Instância do gerenciador de objetos para lidar com JSONs e
     *                   Strings.
     */
    protected Entity(String nome, String ID) {
        this.ID = ID;
        this.nome = nome;
    }

    /**
     * Getter do ID da entidade.
     * 
     * @return String do ID.
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Setter do ID da entidade.
     * @param newID Novo ID a ser colocado na entidade.
     * @return String do ID.
     */
    public String setID(String newID) {
        this.ID = newID;
        return this.ID;
    }

    /**
     * Getter do nome da entidade.
     * 
     * @return String do nome da entidade
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Lista dados de uma entidade. Por exemplo, caso a entidade seja uma playlist,
     * retorna os videos de uma playlist. Caso seja um usuário, retorna objetos playlists,
     * que são as playlists de sua conta.
     * 
     * @param nextPageToken Token usado para fazer requisições sucessivas para listar
     * todas as páginas dos recursos.
     * @return JSON com os dados a serem retornados.
     * @throws Exception caso ocorra qualquer problema ao listar os dados da
     *                   entidade.
     */
    public abstract String listData(String nextPageToken) throws Exception;

}
