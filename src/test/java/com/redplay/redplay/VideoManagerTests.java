package com.redplay.redplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import com.redplay.redplay.managers.VideoManager;

/*
    getStatistics
    getInfo
    listVideoData
    searchVideos
*/

public class VideoManagerTests {
    public VideoManager vm =VideoManager.getInstance();
    @Test
    @Description("Testar o getStatistics")
        public void getStatisticsTest() {
            try {
                VideoManager vm = VideoManager.getInstance();
                System.out.println(vm.getStatistics("9uS4XosYl2A"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new AssertionError("Ocorreu um erro ao selecionar ao tentar pegar as Estatísticas (getStatistics)");
        }
    }

    @Test
    @Description("Testar o listVideoData")
    public void listVideoDataTest(){
        try{
            vm.listVideoData("15 aMostra IME USP - Denis Deratani Maua - Inteligência Artificial", "", "", "", "", "", "", "", "");
        } catch(Exception e){
            e.printStackTrace();
            throw new AssertionError("Ocorreu um erro ao tentar listar as informações do vídeo");
        }
    }
}