package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

    @Password
    private EditText impPass;

    @Email
    private EditText impMail;

    private Validator validate;

    private FirebaseAuth mAuth;

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

        validate = new Validator(this);
        validate.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public void onValidationSucceeded() {
        CallLogin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getApplicationContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(MainActivity.this, "Sem ideia", Toast.LENGTH_SHORT).show();
            }
        }
    }

    View.OnClickListener CreateUser = view -> {
      Intent CreateUser  =new Intent(MainActivity.this, actCadastro.class);
      startActivity(CreateUser);
    };

    View.OnClickListener CallMainScreen = view -> {
        validate.validate();
    };

    public void CallLogin(){
        Toast.makeText(this, "funciona", Toast.LENGTH_SHORT).show();
    }
    


    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.runFinalizersOnExit(true);
    }
}
