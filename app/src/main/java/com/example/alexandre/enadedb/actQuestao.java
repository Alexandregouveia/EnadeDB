package com.example.alexandre.enadedb;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class actQuestao extends AppCompatActivity {

    ScrollView scQuestion;;
    Button btAlternativa ;
    ImageButton btProxima;
    ImageButton btAnterior;
    ImageButton btMainMenu;
    TextView mText;
    ImageView mImage;
    File img;

    String letra;
    String texto;
    String curso;
    String ano;
    String gabarito;
    String[] respostas;

    int i;
    int hits=0;
    boolean last = false;

    ArrayList<Questoes> listaQuestoes = new ArrayList<>();
    List<String> listaRespostas;
    ArrayList<String> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_questao);

        i=0;

        mText = findViewById(R.id.TextQuestion);
        mImage = findViewById(R.id.ImageQuestion);
        btAlternativa = findViewById(R.id.btAlternativas);
        btAlternativa.setOnClickListener(CallAlt);

        btProxima = findViewById(R.id.btNext);
        btProxima.setOnClickListener(CallNext);

        btAnterior = findViewById(R.id.btPrev);
        btAnterior.setOnClickListener(CallPrev);

        btMainMenu = findViewById(R.id.btMenu);
        btMainMenu.setOnClickListener(CallMenu);

        scQuestion = findViewById(R.id.scrollQ);

        try{
            curso = getIntent().getExtras().getString("curso");
            ano = getIntent().getExtras().getString("ano");
        }catch (Exception ex){}

        try{
            img = File.createTempFile("question","png");
        }catch (IOException ex){}

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("gabarito");
        mRef.child(curso).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gabarito = dataSnapshot.getValue(String.class);
                respostas = gabarito.split("#");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
        });

        mRef = FirebaseDatabase.getInstance().getReference("provas");
        mRef.child(curso).child(ano).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Questoes questao = child.getValue(Questoes.class);
                    listaQuestoes.add(questao);
                    Iniciar();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode,resultCode, Data);
        if(requestCode==0 & resultCode==RESULT_OK){
            letra = Data.getStringExtra("Resultado");
            texto = Data.getStringExtra("Texto");
            btAlternativa.setText(texto);
        }
    }

    public void Iniciar(){
        listaRespostas = new ArrayList<>();
        for (int z=0; z<listaQuestoes.size(); z++){
            listaRespostas.add("");
        }//TODO Linha alterada
        mText.setText(listaQuestoes.get(0).getText().replace("\\n", System.getProperty("line.separator")).replace("\\t", "\t"));
        if (listaQuestoes.get(0).getImage()!=null){  //Verifica a presença de imagens
            if (listaQuestoes.get(0).getImage()!=null){
                FirebaseStorage.getInstance().getReference(listaQuestoes.get(0).getImage()).getFile(img)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
                                Picasso.with(actQuestao.this).load(img).skipMemoryCache().into(mImage);
                            }
                        });
            }else{
                mImage.setVisibility(View.INVISIBLE);
            }
        }else{
            mImage.setVisibility(View.INVISIBLE);
        }

    }

    public void Proxima(int indice, boolean last){
        scQuestion.fullScroll(0);


        mText.setText(listaQuestoes.get(indice).getText().replace("\\n", System.getProperty("line.separator"))
                .replace("\n", System.getProperty("line.separator"))
                .replace("\\t","\t"));
        if (listaQuestoes.get(indice).getImage()!=null){
            FirebaseStorage.getInstance().getReference(listaQuestoes.get(indice).getImage()).getFile(img)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            mImage.setVisibility(View.VISIBLE);
//                            mImage.setImageDrawable(Drawable.createFromPath(img.getAbsolutePath()));
                            Picasso.with(actQuestao.this).load(img).skipMemoryCache().into(mImage);
                        }
                    });
        }else{
            mImage.setVisibility(View.INVISIBLE);
        }

        if (last==true){
            btProxima.setOnClickListener(CallEnd);
        }else{
            btProxima.setOnClickListener(CallNext);
        }


        switch (listaRespostas.get(i)){
            case "A":
                letra = "A";
                btAlternativa.setText(listaQuestoes.get(i).getA0());
                listaRespostas.set(i,"A");
                break;
            case "B":
                letra = "B";
                btAlternativa.setText(listaQuestoes.get(i).getA1());
                listaRespostas.set(i,"B");
                break;
            case "C":
                letra = "C";
                btAlternativa.setText(listaQuestoes.get(i).getA2());
                listaRespostas.set(i,"C");
                break;

            case "D":
                letra = "D";
                btAlternativa.setText(listaQuestoes.get(i).getA3());
                listaRespostas.set(i,"D");
                break;

            case "E":
                letra = "E";
                btAlternativa.setText(listaQuestoes.get(i).getA4());
                listaRespostas.set(i,"E");
                break;
            default:
                btAlternativa.setText(getResources().getText(R.string.btAlternativas));
                break;
        }

    }


    View.OnClickListener CallAlt = view -> {

            Intent alt = new Intent(actQuestao.this, actAlternativa.class);
            alt.putExtra("a",listaQuestoes.get(i).getA0());
            alt.putExtra("b",listaQuestoes.get(i).getA1());
            alt.putExtra("c",listaQuestoes.get(i).getA2());
            alt.putExtra("d",listaQuestoes.get(i).getA3());
            alt.putExtra("e",listaQuestoes.get(i).getA4());
            startActivityForResult(alt,0);

    };

    View.OnClickListener CallNext = view -> {

        if (letra!=null){
            listaRespostas.set(i,letra);
            i= i+1;
            if (i==listaQuestoes.size()-1){
                last=true;
            }

            letra = null;
            Proxima(i, last);
        }else{
            Toast.makeText(this, getResources().getText(R.string.selecione), Toast.LENGTH_SHORT).show();
        }

//            i= i+1;
//            if (i==listaQuestoes.size()-1){
//                last=true;
//            }

            letra = null;
            Proxima(i, last);
    };

    View.OnClickListener CallPrev = view -> {
      if (i>0){
          i=i-1;
          Proxima(i,false);
      }else{
          Toast.makeText(this, getResources().getText(R.string.primeira), Toast.LENGTH_SHORT).show();
      }
    };

    View.OnClickListener CallEnd = view -> {
        Intent intentScore = new Intent(actQuestao.this,actScore.class);
        for (int j =0; j<listaQuestoes.size(); j++){
            if (listaRespostas.get(j).equals(respostas[j]) || respostas[j].equals("*") ){
                hits++;
                result.add("O");
            }else {
                result.add("X");
            }

        }
        int score = (100*hits)/listaRespostas.size();
        Historico hist = new Historico();
        hist.setTest_data(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()));
        hist.setAno(Integer.parseInt(ano));
        hist.setScore(score);
        intentScore.putExtra("partida", hist);
        intentScore.putStringArrayListExtra("acertos",result);
        startActivity(intentScore);
    };

    View.OnClickListener CallMenu = view -> {
        Intent Main = new Intent(actQuestao.this,actMainMenu.class);
        startActivity(Main);
    };


}
