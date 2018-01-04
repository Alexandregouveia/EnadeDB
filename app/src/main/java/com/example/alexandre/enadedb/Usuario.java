package com.example.alexandre.enadedb;

import java.util.ArrayList;

/**
 * Created by alexandre on 16/11/17.
 */

public class Usuario  {


    String name;
    String lastName;
    String instensino;
    String curso;
    ArrayList<Historico> historico;
    int grupo;

    public Usuario(){}

    public Usuario(String name, String lastName, String instensino, String curso, int grupo) {
        this.name = name;
        this.lastName = lastName;
        this.instensino = instensino;
        this.curso = curso;
        this.grupo = grupo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstensino() {
        return instensino;
    }

    public void setInstensino(String instensino) {
        this.instensino = instensino;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public ArrayList<Historico> getHistorico() {
        return historico;
    }

    public void setHistorico(ArrayList<Historico> historico) {
        this.historico = historico;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
}