package com.example.alexandre.enadedb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
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

    public static final int PICK_IMAGE=1;
    EditText ImpEmail;
    TextView titulo;
    Button Cadastrar;
    ImageButton btFoto;

    int flag = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cadastro);

        ImpEmail = findViewById(R.id.ImpEmail);
        titulo = findViewById(R.id.Atualizar);
        Cadastrar = findViewById(R.id.btCadastrar);
        Cadastrar.setOnClickListener(cadastrar);
        btFoto = findViewById(R.id.btFoto);
        btFoto.setOnClickListener(TirarFoto);

        Spinner SpCursos = findViewById(R.id.spCursos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpCursos.setAdapter(adapter);

        //Reaproveita a tela de cadastro para atualizar os dados

        flag = getIntent().getIntExtra("update",0);
        if (flag == 1){
            titulo.setText("Atualizar informações");
            ImpEmail.setFocusable(false);
            Cadastrar.setText("Atualizar");
            Cadastrar.setOnClickListener(AtualizarDados);
        }

    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data){
        if (requsetCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ){
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");
            btFoto.setImageBitmap(foto);

        }
    }

    View.OnClickListener TirarFoto = view ->{
      showPictureDialog();
    };

    View.OnClickListener AtualizarDados = view -> {
        Intent MainScreen = new Intent(actCadastro.this, actMainMenu.class);
        startActivity(MainScreen);
    };

    View.OnClickListener cadastrar = view -> {
        Intent LoginScreen = new Intent(actCadastro.this, MainActivity.class);
        startActivity(LoginScreen);
        //Usuario novoUser = new Usuario();
    };

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                getString(R.string.Galeria),
                getString(R.string.Camera )};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE);
    }


}
