package com.example.alexandre.enadedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

    private FirebaseAuth mAuth;

    @Password
    private EditText impPass;

    @Email
    private EditText impMail;

    private Validator validate;
    private ProgressDialog loading;


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
        
        Button btReset = findViewById(R.id.btReset);
        btReset.setOnClickListener(CallReset);

        validate = new Validator(this);
        validate.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();




    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            Intent logado = new Intent(this,actMainMenu.class);
            startActivity(logado);
        }
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
        Login login= new Login();
        login.execute(null,null,null);
    }
    


    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.runFinalizersOnExit(true);
    }

    public  class Login extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MainActivity.this);
            loading.setMessage(getResources().getString(R.string.loadingLogin));
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAuth.signInWithEmailAndPassword(impMail.getText().toString(), impPass.getText().toString())
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent logado = new Intent(MainActivity.this, actMainMenu.class);
                            startActivity(logado);
                        }
                    })
                    .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, getResources().getText(R.string.erroLogin), Toast.LENGTH_SHORT).show();
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
        }
    }
    
    View.OnClickListener CallReset = view -> {
        try {
            mAuth.sendPasswordResetEmail(impMail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, "Email enviado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (NullPointerException ex){
            Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
        }
    };
}

