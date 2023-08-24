package com.redplay.redplay.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;


/**
 * Classe unificada para operações que envolvam objetos como Strings e JSONs
 */
public class ObjectManager {
    /**
     * Lê um arquivo .JSON e guarda o seu conteúdo em um JSONObject.
     * 
     * @param filePath Caminho para arquivo .JSON a ser lido e transformado em
     *                 JSONObject.
     * @return Objeto JSON (JSONObject) do arquivo.
     * @throws IOException caso não seja possível encontrar um arquivo.
     */
    public JSONObject readFile(String filePath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        /* Lê o arquivo linha a linha e adiciona ao StringBuilder */
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                fileContent.append(fileLine);
            }
        }
        /* Converte a String lida para JSONObject */
        return new JSONObject(fileContent.toString());
    }

    /**
     * Recebe uma String (normalmente uma resposta de um servidor) e
     * usa expressões regulares para obter os seus campos, retornando uma lista com
     * o valor de cada um desses campos
     * 
     * @param stringToRegEx String de um JSON onde serão identificados os padrões
     *                      com RegEx.
     * @param regex         Expressão regular usada para analisar os padrões da
     *                      stringToRegEx.
     * 
     * @return ArrayList contendo os valores dos campos identificados com o
     *         padrão do parâmetro regex.
     */
    public ArrayList<String> selectJSONFieldsWithRegEx(String stringToRegEx, String regex) {
        ArrayList<String> items = new ArrayList<String>();

        /* Faz o match do RegEx na String */
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToRegEx.toString());

        /* Compila todos os campos encontrados em uma ArrayList e retorna */
        while (matcher.find()) {
            items.add(matcher.group(1));
        }
        return items;
    }
}