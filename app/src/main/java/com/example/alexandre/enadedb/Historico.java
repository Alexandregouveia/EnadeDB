package com.example.alexandre.enadedb;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexandre on 18/11/17.
 */

public class Historico {
    private String test_data;
    private int score;
    private int ano;


    public Historico(String test_data, int score, int ano) {
        this.test_data = test_data;
        this.score = score;
        this.ano = ano;
    }

    public String getTest_data() {
        return test_data;
    }

    public void setTest_data(String test_data) {
        this.test_data = test_data;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}