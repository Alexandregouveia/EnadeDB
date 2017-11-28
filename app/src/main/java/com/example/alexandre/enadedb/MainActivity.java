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
      startActivity(CreateUser);
    };

    View.OnClickListener CallQuestion = view -> {
        Intent question = new Intent(MainActivity.this, actMainMenu.class);
        startActivity(question);
    };

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.runFinalizersOnExit(true);
    }
}
