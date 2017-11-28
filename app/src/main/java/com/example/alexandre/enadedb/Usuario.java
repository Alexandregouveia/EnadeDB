package com.example.alexandre.enadedb;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by alexandre on 16/11/17.
 */

public class Usuario implements Parcelable {


    String name;
    String lastName;
    String email;
    String instensino;
    String curso;



    public Usuario(String name, String lastName, String email, String instensino, String curso,
                   ArrayList<Historico> hist) {
        super();
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.instensino = instensino;
        this.curso = curso;
        this.hist = hist;
    }

    ArrayList<Historico> hist;
    public ArrayList<Historico> getHist() {
        return hist;
    }
    public void setHist(ArrayList<Historico> hist) {
        this.hist = hist;
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



    protected Usuario(Parcel in) {
        name = in.readString();
        lastName = in.readString();
        email = in.readString();
        instensino = in.readString();
        curso = in.readString();
        if (in.readByte() == 0x01) {
            hist = new ArrayList<Historico>();
            in.readList(hist, Historico.class.getClassLoader());
        } else {
            hist = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(instensino);
        dest.writeString(curso);
        if (hist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(hist);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}