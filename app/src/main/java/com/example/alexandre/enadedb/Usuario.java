package com.example.alexandre.enadedb;

/**
 * Created by alexandre on 16/11/17.
 */

public class Usuario {
   private String name;
   private String lastName;
   private String email;
   private String course;
   private String instEnsino;


    public Usuario(String name, String lastName, String email, String course, String instEnsino) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.course = course;
        this.instEnsino = instEnsino;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstEnsino() {
        return instEnsino;
    }

    public void setInstEnsino(String instEnsino) {
        this.instEnsino = instEnsino;
    }
}
