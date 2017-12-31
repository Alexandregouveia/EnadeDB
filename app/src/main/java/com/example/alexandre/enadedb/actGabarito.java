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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.valueOf;

public class actGabarito extends AppCompatActivity {

    TextView qNumber;
    TextView qLetter;
    Button btMainM;
    GridView gabView;


    String curso;
    String ano;
    String gabarito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_gabarito);
        qNumber = findViewById(R.id.qNumber);
        qLetter = findViewById(R.id.qLetter);
        btMainM = findViewById(R.id.btMainMenu);
        btMainM.setOnClickListener(CallMainMenu);
        try{
            curso = getIntent().getExtras().getString("curso");
            ano = getIntent().getExtras().getString("ano");
        }catch (Exception ex){}

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("gabarito");
        mRef.child(curso).child(ano).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gabarito = dataSnapshot.getValue(String.class);
                String[] respostas = gabarito.split("#");
                showPopUp(respostas);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
    public void showPopUp(String[] resposta){
        int numberItems = 35;
        ArrayList<Resposta> respostas= new ArrayList<>();

        for (int x = 0; x<resposta.length; x++){
            respostas.add(new Resposta(valueOf(x+1),resposta[x]));
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
