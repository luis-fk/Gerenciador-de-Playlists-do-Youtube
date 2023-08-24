package com.redplay.redplay;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

import com.redplay.redplay.managers.ObjectManager;

class ObjectManagerTests {
    final static ObjectManager objManager = new ObjectManager();

    @Test
    @Description("Testa se um arquivo pode ser lido")
    void readFileTest() {
        try {
            assertNotNull(
                    objManager.readFile("./src/main/java/com/redplay/redplay/resources/test.json"),
                    "Não conseguiu ler arquivo!");

        } catch (Exception e) {
            Assertions.fail("An exception occurred!", e);
        }
    }

    @Test
    @Description("Testa se consegue ler o conteudo de um JSON usando RegEx")
    void selectJSONFieldsWithRegExTest() {
        try {
             ArrayList<String> item = new ArrayList<String>();

             String jsonString = "{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\","+
                                 "\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\","+
                                 "\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\","+
                                 "\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language,"+
                                 "used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},"+
                                 "\"GlossSee\":\"markup\"}}}}}";
             String[] parameters = { "\"ID\":\"(.*?)\"",
                                     "\"Abbrev\":\"(.*?)\"" };
            
            item.add(objManager.selectJSONFieldsWithRegEx(jsonString, parameters[0]).toString());
            item.add(objManager.selectJSONFieldsWithRegEx(jsonString, parameters[1]).toString());
            assertEquals("[SGML]", item.get(0), "ID deve retornar certo");
            assertEquals("[ISO 8879:1986]", item.get(1), "Abbrev deve retornar certo");

        } catch (Exception e) {
            Assertions.fail("ID não retornou certo!", e);
        }
    }
}

