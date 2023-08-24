package com.redplay.redplay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import com.redplay.redplay.entities.Playlist;
import com.redplay.redplay.entities.Usuario;
import com.redplay.redplay.managers.ObjectManager;
import com.redplay.redplay.managers.VideoManager;

public class PlaylistTests {
    public Playlist playlist;
    public ArrayList<String> visibilidade1;
    public ArrayList<String> visibilidade2;

    @BeforeEach
    public void setup(){      
        playlist = new Playlist("", "PLgZGNwIFLuLru1Od2cEuUEaMsYlRUgJ2B");
    }
    @Test
    @Description("Teste para o método requestToAddVideo")
    public void requestToAddVideoTest() {
        try {
            playlist.requestToAddVideo("F2Jyz-bQNVA");
        } catch (Exception e) {
            throw new AssertionError("Falha ao adicionar video");
        }
    }

    @Test
    @Description("Teste para o método listData")
    public void listDataTest() {
        try {
            playlist.listData("");
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro");
        }
    }

    @Test
    @Description("Teste para o método listPlaylistVideos")
    public void listPlaylistVideosTest() {
        try {
            playlist.listPlaylistVideos();
        } catch (Exception e) {
            throw new AssertionError("Ocorreu um erro");
        }

    }
    @Test
    @Description("Teste para o método selectVideos")
    public void selectVideosTest() {
        try {
            String input = "4";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            Scanner sc = new Scanner(System.in);
            sc.close();
        } catch (Exception e) {
            throw new AssertionError("Erro ao selecionar os vídeos");
        }
    }
}

