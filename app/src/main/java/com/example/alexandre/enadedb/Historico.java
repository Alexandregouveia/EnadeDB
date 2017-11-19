package com.example.alexandre.enadedb;

import java.util.Date;

/**
 * Created by alexandre on 18/11/17.
 */

public class Historico {
    private Date data;
    private double score;
    private int ano;

    public Historico(Date data, double score, int ano) {
        this.data = data;
        this.score = score;
        this.ano = ano;
    }

    public Date getData() {
        return data;
    }

    public double getScore() {
        return score;
    }

    public int getAno() {
        return ano;
    }
}
