package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    EditText impPass;
    EditText impMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        impPass = findViewById(R.id.ImpPassw);
        impMail = findViewById(R.id.ImpMail);

        Button Login  = findViewById(R.id.btLogin);
        Login.setOnClickListener(CallMainScreen);

        Button NewUser = findViewById(R.id.btNewUser);
        NewUser.setOnClickListener(CreateUser);





    }



    View.OnClickListener CreateUser = view -> {
      Intent CreateUser  =new Intent(MainActivity.this, actCadastro.class);
      startActivity(CreateUser);
    };

    View.OnClickListener CallMainScreen = view -> {
        Intent question = new Intent(MainActivity.this, actMainMenu.class);
        startActivity(question);
    };



    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.runFinalizersOnExit(true);
    }
}
