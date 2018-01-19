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
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class actScore extends AppCompatActivity {
    TextView points;
    Button MainM;
    GridView gridQuestions;

    Historico partida;
    ArrayList<String> acertos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_score);

        Button MainM = findViewById(R.id.btMain);
        MainM.setOnClickListener(CallMainMenu);

        gridQuestions = findViewById(R.id.acertos);

        points = findViewById(R.id.txtScore);

        try {
            partida = getIntent().getExtras().getParcelable("partida");
            acertos = getIntent().getExtras().getStringArrayList("acertos");
            showPopUp(acertos);
            points.setText(partida.getScore() + "% ");
        }catch (Exception ex){}

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    View.OnClickListener CallMainMenu = view -> {
        Intent mScreen = new Intent(actScore.this,actMainMenu.class);
        mScreen.putExtra("atual", partida);
        startActivity(mScreen);
    };



    static class ViewHolder{
        TextView Number;
        TextView Letter;
        TextView txtQ;

    }

    public class cardAdapter extends BaseAdapter {
        private ArrayList<actScore.Resposta> listaResp;
        private LayoutInflater layoutInflater;

        public cardAdapter(Context context, ArrayList<actScore.Resposta> arraylist){
            listaResp = arraylist;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return listaResp.size();
        }

        @Override
        public actScore.Resposta getItem(int i){
            return listaResp.get(i);
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            actScore.ViewHolder viewHolder;

            if (view==null){
                viewHolder = new actScore.ViewHolder();

                view = layoutInflater.inflate(R.layout.activity_gab_card, parent, false);
                viewHolder.Number = view.findViewById(R.id.qNumber);
                viewHolder.Letter = view.findViewById(R.id.qLetter);
                viewHolder.txtQ = view.findViewById(R.id.txtQ);
                view.setTag(viewHolder);

            }else{
                viewHolder = (actScore.ViewHolder) view.getTag();
            }


            actScore.Resposta resposta = getItem(position);
            if (resposta!=null){
                viewHolder.Number.setText(resposta.getNumber());
                viewHolder.Letter.setText(resposta.getLetter());
                if (resposta.getLetter().equals("X")){
                    viewHolder.Number.setBackgroundColor(getColor(R.color.low));
                    viewHolder.Letter.setBackgroundColor(getColor(R.color.low));
                    viewHolder.txtQ.setBackgroundColor(getColor(R.color.low));
                }else{
                    viewHolder.Number.setBackgroundColor(getColor(R.color.high));
                    viewHolder.Letter.setBackgroundColor(getColor(R.color.high));
                    viewHolder.txtQ.setBackgroundColor(getColor(R.color.high));
                }

            }

            return view;
        }



    }
    public void showPopUp(ArrayList<String> resp){
        ArrayList<actScore.Resposta> respostas= new ArrayList<>();

        for (int x = 0; x<acertos.size(); x++){
            respostas.add(new actScore.Resposta(valueOf(x+1),acertos.get(x)));
        }

        gridQuestions.setAdapter(new actScore.cardAdapter(getApplicationContext(),respostas));


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
}
