package com.example.alexandre.enadedb;

/**
 * Created by alexandre on 02/01/18.
 */

public class Questoes {
   private String text;
   private String A0;
   private String A1;
   private String A2;
   private String A3;
   private String A4;
   private String image;

    public Questoes() {

    }

    public Questoes(String text, String a0, String a1, String a2, String a3, String a4, String image) {
        this.text = text;
        A0 = a0;
        A1 = a1;
        A2 = a2;
        A3 = a3;
        A4 = a4;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getA0() {
        return A0;
    }

    public void setA0(String a0) {
        A0 = a0;
    }

    public String getA1() {
        return A1;
    }

    public void setA1(String a1) {
        A1 = a1;
    }

    public String getA2() {
        return A2;
    }

    public void setA2(String a2) {
        A2 = a2;
    }

    public String getA3() {
        return A3;
    }

    public void setA3(String a3) {
        A3 = a3;
    }

    public String getA4() {
        return A4;
    }

    public void setA4(String a4) {
        A4 = a4;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
