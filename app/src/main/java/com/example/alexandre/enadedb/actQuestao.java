package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class actQuestao extends AppCompatActivity {

    Button btAlternativa ;
    Button btProxima;
    TextView mText;
    ImageView mImage;

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
    ArrayList<String> listaRespostas = new ArrayList<>();

    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_questao);

        i=0;

        mText = findViewById(R.id.TextQuestion);
        mImage = findViewById(R.id.ImageQuestion);
        btAlternativa = findViewById(R.id.btAlternativas);
        btProxima = findViewById(R.id.btProx);
        btAlternativa.setOnClickListener(CallAlt);
        btProxima.setOnClickListener(CallNext);

        try{
            curso = getIntent().getExtras().getString("curso");
            ano = getIntent().getExtras().getString("ano");
        }catch (Exception ex){}

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
//       super.onBackPressed();
        Intent mainScreen = new Intent(actQuestao.this,actMainMenu.class);
        startActivity(mainScreen);

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
        mText.setText(listaQuestoes.get(0).getText());

    }
    public void Proxima(int indice, boolean last){
        btAlternativa.setText(getResources().getText(R.string.btAlternativas));
        mText.setText(listaQuestoes.get(indice).getText());
        if (last==true){
            btProxima.setText(getResources().getText(R.string.finalizar));
            btProxima.setOnClickListener(CallEnd);
        }
    }

    View.OnClickListener CallEnd = view -> {
          Intent score = new Intent(actQuestao.this,actScore.class);
          for (int j =0; j<listaQuestoes.size(); j++){
              if (listaRespostas.get(j).equals(respostas[j]));
              hits++;
          }
          score.putExtra("score",hits);
          startActivity(score);
    };

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
            i= i+1;
            if (i==listaQuestoes.size()-1){
                last=true;
            }
            listaRespostas.add(letra);
            letra = null;
            Proxima(i, last);
        }else{
            Toast.makeText(this, getResources().getText(R.string.selecione), Toast.LENGTH_SHORT).show();
        }

    };




}
