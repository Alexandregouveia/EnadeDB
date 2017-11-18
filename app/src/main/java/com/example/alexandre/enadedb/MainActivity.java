package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Login  = findViewById(R.id.btLogin);
        Button NewUser = findViewById(R.id.btNewUser);
        NewUser.setOnClickListener(CreateUser);
        Login.setOnClickListener(CallQuestion);
    }

    View.OnClickListener CreateUser = view -> {
      Intent CreateUser  =new Intent(MainActivity.this, actCadastro.class);
      Log.i("Botao pressionado","Aqui");
      startActivity(CreateUser);
    };

    View.OnClickListener CallQuestion = view -> {
        Intent question = new Intent(MainActivity.this, actMainMenu.class);
        startActivity(question);
    };


}
