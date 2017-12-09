package com.example.alexandre.enadedb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.valueOf;

public class actGabarito extends AppCompatActivity {

    TextView qNumber;
    TextView qLetter;
    Button btMainM;
    GridView gabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_gabarito);
        qNumber = findViewById(R.id.qNumber);
        qLetter = findViewById(R.id.qLetter);
        btMainM = findViewById(R.id.btMainMenu);
        btMainM.setOnClickListener(CallMainMenu);
        showPopUp();
    }

    static class ViewHolder{
        TextView Number;
        TextView Letter;

    }

    public class cardAdapter extends BaseAdapter {
        private ArrayList<Resposta> listaResp;
        private LayoutInflater layoutInflater;

        public cardAdapter(Context context, ArrayList<Resposta> arraylist){
            listaResp = arraylist;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return listaResp.size();
        }

        @Override
        public Resposta getItem(int i){
            return listaResp.get(i);
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            actGabarito.ViewHolder viewHolder;

            if (view==null){
                viewHolder = new actGabarito.ViewHolder();

                view = layoutInflater.inflate(R.layout.activity_gab_card, parent, false);
                viewHolder.Number = view.findViewById(R.id.qNumber);
                viewHolder.Letter = view.findViewById(R.id.qLetter);
                view.setTag(viewHolder);

            }else{
                viewHolder = (actGabarito.ViewHolder) view.getTag();
            }


            Resposta resposta = getItem(position);
            if (resposta!=null){
                viewHolder.Number.setText(resposta.getNumber());
                viewHolder.Letter.setText(resposta.getLetter());

            }

            return view;
        }



    }
    public void showPopUp(){
        int numberItems = 35;
        ArrayList<Resposta> respostas= new ArrayList<>();

        for (int x = 0; x<numberItems; x++){
            respostas.add(new Resposta(valueOf(x+1),"A"));
        }

        gabView = findViewById(R.id.gabView);
        gabView.setAdapter(new actGabarito.cardAdapter(actGabarito.this,respostas));

    }

    public class Resposta{
        String number;
        String letter;

        public Resposta(String number, String letter) {
            this.number = number;
            this.letter = letter;

        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }
    }

    View.OnClickListener CallMainMenu = view -> {
        Intent MainMenu = new Intent(actGabarito.this, actMainMenu.class);
        startActivity(MainMenu);
    };
}
