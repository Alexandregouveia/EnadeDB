package com.example.alexandre.enadedb;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.media.MediaRecorder.VideoSource.CAMERA;



public class actCadastro extends AppCompatActivity {

    File file;
    Uri imguri;
    EditText ImpEmail;
    EditText ImpName;
    EditText ImpLastName;
    EditText ImpInstEnsino;
    Spinner SpCursos;
    TextView titulo;
    Button Cadastrar;
    ImageButton btFoto;
    Bundle extras;
    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cadastro);

        //Checa permissão para acesar a camera
        int permission = ContextCompat.checkSelfPermission(actCadastro.this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(actCadastro.this,new String[]{Manifest.permission.CAMERA},1);
        }


        ImpName = findViewById(R.id.ImpName);
        ImpLastName = findViewById(R.id.ImpLastName);
        ImpInstEnsino = findViewById(R.id.ImpEnsino);
        ImpEmail = findViewById(R.id.ImpEmail);
        titulo = findViewById(R.id.Atualizar);

        Cadastrar = findViewById(R.id.btCadastrar);
        Cadastrar.setOnClickListener(cadastrar);
        btFoto = findViewById(R.id.btFoto);
        btFoto.setOnClickListener(TirarFoto);

        SpCursos = findViewById(R.id.spCursos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpCursos.setAdapter(adapter);

        
        //Reaproveita a tela de cadastro para atualizar os dados

        flag = getIntent().getIntExtra("update",0);
        if (flag == 1){
            titulo.setText(R.string.update);
            ImpEmail.setFocusable(false);
            Cadastrar.setText(R.string.btupdate);
            Cadastrar.setOnClickListener(AtualizarDados);
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
                Bundle img = data.getExtras();
                Bitmap image = img.getParcelable("data");
                btFoto.setImageBitmap(image);
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
        Intent LoginScreen = new Intent(actCadastro.this, MainActivity.class);
        startActivity(LoginScreen);
        Usuario user = new Usuario(ImpName.getText().toString(),
                                   ImpLastName.getText().toString(),
                                   ImpEmail.getText().toString(),
                                   ImpInstEnsino.getText().toString(),
                                   SpCursos.getSelectedItem().toString(),
                                   null
                                   );
        Toast.makeText(this, user.getName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();
    };



    private void takePhotoFromCamera() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                "file"+String.valueOf(System.currentTimeMillis())+".jpg");
        imguri = Uri.fromFile(file);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
        camIntent.putExtra("return-data",true);
        startActivityForResult(camIntent,0);
    }


    private void cropImg(){

        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imguri,"image/*");

            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("outputX",140);
            cropIntent.putExtra("outputY",140);
            cropIntent.putExtra("aspectX",3);
            cropIntent.putExtra("aspectY",4);
            cropIntent.putExtra("scaleUpIfNeeded",true);
            cropIntent.putExtra("return-data",true);
            startActivityForResult(cropIntent,1);
        }catch (ActivityNotFoundException e){

        }
    }


}
