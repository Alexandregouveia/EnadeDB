package com.example.alexandre.enadedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class actCadastro extends AppCompatActivity {

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
      Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (takePicture.resolveActivity(getPackageManager())!= null){
          startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
      }
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
}
