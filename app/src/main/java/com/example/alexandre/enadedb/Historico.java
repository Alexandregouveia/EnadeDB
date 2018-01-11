package com.example.alexandre.enadedb;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexandre on 18/11/17.
 */

public class Historico implements Parcelable {
    private String test_data;
    private int score;
    private int ano;

    public Historico(){}

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

    public int getScore() {
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

    protected Historico(Parcel in) {
        test_data = in.readString();
        score = in.readInt();
        ano = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(test_data);
        dest.writeInt(score);
        dest.writeInt(ano);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Historico> CREATOR = new Parcelable.Creator<Historico>() {
        @Override
        public Historico createFromParcel(Parcel in) {
            return new Historico(in);
        }

        @Override
        public Historico[] newArray(int size) {
            return new Historico[size];
        }
    };
}