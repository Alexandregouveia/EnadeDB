package com.example.alexandre.enadedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class actAlternativa extends AppCompatActivity {

    Button btSelect;
    RadioGroup Alternativas;
    RadioButton AltSelecionada;
    RadioButton altA;
    RadioButton altB;
    RadioButton altC;
    RadioButton altD;
    RadioButton altE;

    String txtA;
    String txtB;
    String txtC;
    String txtD;
    String txtE;

    File imgA;
    File imgB;
    File imgC;
    File imgD;
    File imgE;

    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_alternativa);
        Alternativas = findViewById(R.id.alternativas);
        btSelect = findViewById(R.id.btConfirm);
        btSelect.setOnClickListener(ConfAlternativa);

        altA = findViewById(R.id.alta);
        altB = findViewById(R.id.altb);
        altC = findViewById(R.id.altc);
        altD = findViewById(R.id.altd);
        altE = findViewById(R.id.alte);

        txtA = getIntent().getExtras().getString("a");
        txtB = getIntent().getExtras().getString("b");
        txtC = getIntent().getExtras().getString("c");
        txtD = getIntent().getExtras().getString("d");
        txtE = getIntent().getExtras().getString("e");

        if (IsPicture(txtA)){
            try {
                imgA = File.createTempFile("altA","png");
                imgB = File.createTempFile("altB","png");
                imgC = File.createTempFile("altC","png");
                imgD = File.createTempFile("altD","png");
                imgE = File.createTempFile("altE","png");
            }catch (IOException ex){}

            FirebaseStorage.getInstance().getReference(txtA).getFile(imgA)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            altA.setButtonDrawable(Drawable.createFromPath(imgA.getAbsolutePath()));

                        }
                    });

            FirebaseStorage.getInstance().getReference(txtB).getFile(imgB)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            altB.setButtonDrawable(Drawable.createFromPath(imgB.getAbsolutePath()));

                        }
                    });

            FirebaseStorage.getInstance().getReference(txtC).getFile(imgC)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            altC.setButtonDrawable(Drawable.createFromPath(imgC.getAbsolutePath()));
                        }
                    });

            FirebaseStorage.getInstance().getReference(txtD).getFile(imgD)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            altD.setButtonDrawable(Drawable.createFromPath(imgD.getAbsolutePath()));
                        }
                    });

            FirebaseStorage.getInstance().getReference(txtE).getFile(imgE)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            altE.setButtonDrawable(Drawable.createFromPath(imgE.getAbsolutePath()));
                        }
                    });

        }else{
            altA.setText(getIntent().getExtras().getString("a"));
            altB.setText(getIntent().getExtras().getString("b"));
            altC.setText(getIntent().getExtras().getString("c"));
            altD.setText(getIntent().getExtras().getString("d"));
            altE.setText(getIntent().getExtras().getString("e"));
        }
        Log.d("msg: ", String.valueOf(IsPicture(txtA)));
    }

    View.OnClickListener ConfAlternativa = view -> {
        AltSelecionada = findViewById(Alternativas.getCheckedRadioButtonId());
        if (AltSelecionada ==null){
            Toast.makeText(this, "Escolha uma alternativa", Toast.LENGTH_SHORT).show();
        }else {
            String Letra = AltSelecionada.getTag().toString();
            String texto = AltSelecionada.getText().toString();
//            Toast.makeText(this, Letra, Toast.LENGTH_SHORT).show();
            Intent Retorno = new Intent(actAlternativa.this, actQuestao.class);
            Retorno.putExtra("Resultado", Letra);
            Retorno.putExtra("Texto", texto);
            setResult(RESULT_OK, Retorno);
            finish();
        }
    };

    public boolean IsPicture(String input){
        String[] extsension = input.split("\\.");
        try {
            if (extsension[extsension.length -1].equals("png")){
                return true;
            }else{
                return false;
            }
        }catch (IndexOutOfBoundsException ex){
            return false;
        }
    }
}
