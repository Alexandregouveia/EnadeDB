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

        try {
            altA.setText(getIntent().getExtras().getString("a"));
            altB.setText(getIntent().getExtras().getString("b"));
            altC.setText(getIntent().getExtras().getString("c"));
            altD.setText(getIntent().getExtras().getString("d"));
            altE.setText(getIntent().getExtras().getString("e"));
        }catch (Exception ex){}

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
}
