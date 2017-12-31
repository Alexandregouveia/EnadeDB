package com.example.alexandre.enadedb;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class actCadastro extends AppCompatActivity implements Validator.ValidationListener{

    File file;
    Uri imguri;




    @Email
    EditText ImpEmail;
    @NotEmpty
    EditText ImpName;
    @NotEmpty
    EditText ImpLastName;
    @NonNull
    EditText ImpInstEnsino;

    @Length(min=6)
    @Password
    EditText ImpPass;
    @NonNull
    @ConfirmPassword
    EditText ImpConfPass;

    Spinner SpCursos;
    TextView titulo;
    Button Cadastrar;
    ImageButton btFoto;

    int flag = 0;

    private Validator validator;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cadastro);

        ImpName = findViewById(R.id.ImpName);
        ImpLastName = findViewById(R.id.ImpLastName);
        ImpInstEnsino = findViewById(R.id.ImpEnsino);
        ImpEmail = findViewById(R.id.ImpEmail);
        titulo = findViewById(R.id.Atualizar);
        ImpPass = findViewById(R.id.ImpPass);
        ImpConfPass = findViewById(R.id.ImpConfPass);

        Cadastrar = findViewById(R.id.btCadastrar);
        Cadastrar.setOnClickListener(cadastrar);
        btFoto = findViewById(R.id.btFoto);
        btFoto.setOnClickListener(TirarFoto);

        SpCursos = findViewById(R.id.spCursos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpCursos.setAdapter(adapter);
        SpCursos.setSelection(0);

        mAuth = FirebaseAuth.getInstance();

        
        //Reaproveita a tela de cadastro para atualizar os dados

        flag = getIntent().getIntExtra("update",0);
        if (flag == 1){
            titulo.setText(R.string.update);
            ImpEmail.setFocusable(false);
            Cadastrar.setText(R.string.btupdate);
            Cadastrar.setOnClickListener(AtualizarDados);
        }

        //Checa permissão para acesar a camera
        int permission = ContextCompat.checkSelfPermission(actCadastro.this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(actCadastro.this,new String[]{Manifest.permission.CAMERA},1);
        }
        //Checa permissão para acessar dados do dispositivo
        int permissionStorage = ContextCompat.checkSelfPermission(actCadastro.this,Manifest.permission_group.STORAGE);
        if (permission==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(actCadastro.this,new String[]{Manifest.permission_group.STORAGE},3);
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

    }

        @Override
        public void onValidationSucceeded() {
            Cadastrar();

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
                    Toast.makeText(actCadastro.this, "Sem ideia", Toast.LENGTH_SHORT).show();
                }
            }
        }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imguri != null) {
            outState.putString("cameraImageUri", imguri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            imguri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode== 0 && resultCode == RESULT_OK){

            cropImg();
        }else if (requestCode==2){
            if (data !=null){
                imguri = data.getData();
                cropImg();
            }
        }
        else if (requestCode==1){
            if (data !=null){
                Uri imageUri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                    btFoto.setImageBitmap(image);
                } catch (IOException ex){

                }

            }
        }
    }

    View.OnClickListener TirarFoto = view ->{
        takePhotoFromCamera();
    };

    View.OnClickListener AtualizarDados = view -> {
        Intent MainScreen = new Intent(actCadastro.this, actMainMenu.class);
        startActivity(MainScreen);
    };

    View.OnClickListener cadastrar = view -> {

        validator.validate();
    };





    private void Cadastrar(){

        mAuth.createUserWithEmailAndPassword(ImpEmail.getText().toString(),ImpPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser newUser = mAuth.getCurrentUser();
                Usuario usuario = new Usuario(ImpName.getText().toString(),
                        ImpLastName.getText().toString(),
                        ImpInstEnsino.getText().toString(),
                        SpCursos.getSelectedItem().toString(),
                        newUser.getUid()+"/"+file.getName()
                );
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference("users");
                mStorage = FirebaseStorage.getInstance();
                mStorageRef = mStorage.getReference("photos/"+newUser.getUid()+"/enadedb.jpg");
                Uri photo = Uri.fromFile(file);
                mRef.child(newUser.getUid()).setValue(usuario);
                mStorageRef.putFile(photo);
                Log.d("msg cadastro","Deu certo!");


            }
        });
        Intent LoginScreen = new Intent(actCadastro.this, MainActivity.class);
        startActivity(LoginScreen);



        //Toast.makeText(this, user.getInstensino(), Toast.LENGTH_SHORT).show();
    }

    private void takePhotoFromCamera() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "EnadeDb.jpg");


        }catch (Exception ex){
            file.delete();
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "EnadeDb.jpg");
        }

        String authoroty = getApplicationContext().getPackageName() +".fileprovider";
        imguri = FileProvider.getUriForFile(this,authoroty,file);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
        camIntent.putExtra("return-data",true);
        startActivityForResult(camIntent,0);
    }


    private void cropImg(){

        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imguri,"image/*");

            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("outputX",140);
            cropIntent.putExtra("outputY",160);
            cropIntent.putExtra("aspectX",3);
            cropIntent.putExtra("aspectY",4);
            cropIntent.putExtra("scaleUpIfNeeded",true);
            cropIntent.putExtra("return-data",true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
            startActivityForResult(cropIntent,1);
        }catch (ActivityNotFoundException e){

        }
    }



}
