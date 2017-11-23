package com.example.alexandre.enadedb;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by alexandre on 16/11/17.
 */

public class Usuario implements Parcelable {
    private String name;
    private String lastName;
    private String email;
    private String course;
    private String instEnsino;
    private ArrayList<Historico> historico;

    protected Usuario(Parcel in) {
        name = in.readString();
        lastName = in.readString();
        email = in.readString();
        course = in.readString();
        instEnsino = in.readString();
        if (in.readByte() == 0x01) {
            historico = new ArrayList<Historico>();
            in.readList(historico, Historico.class.getClassLoader());
        } else {
            historico = null;
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
        dest.writeString(course);
        dest.writeString(instEnsino);
        if (historico == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(historico);
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