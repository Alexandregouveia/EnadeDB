package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class actAlternativa extends AppCompatActivity {

    Button btSelect;
    RadioGroup Alternativas;
    RadioButton AltSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_alternativa);
        Alternativas = findViewById(R.id.alternativas);
        btSelect = findViewById(R.id.btConfirm);
        btSelect.setOnClickListener(ConfAlternativa);
    }

    View.OnClickListener ConfAlternativa = view -> {
        AltSelecionada = findViewById(Alternativas.getCheckedRadioButtonId());
        if (AltSelecionada ==null){
            Toast.makeText(this, "Escolha uma alternativa", Toast.LENGTH_SHORT).show();
        }else {
            String Letra = AltSelecionada.getTag().toString();
            String texto = AltSelecionada.getText().toString();
            Toast.makeText(this, Letra, Toast.LENGTH_SHORT).show();
            Intent Retorno = new Intent(actAlternativa.this, actQuestao.class);
            Retorno.putExtra("Resultado", Letra);
            Retorno.putExtra("Texto", texto);
            setResult(RESULT_OK, Retorno);
            finish();
        }
    };
}
