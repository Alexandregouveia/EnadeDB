package com.example.alexandre.enadedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class actScore extends AppCompatActivity {
    TextView points;
    Button MainM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_score);

        Button MainM = findViewById(R.id.btMain);
        MainM.setOnClickListener(CallMainMenu);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    View.OnClickListener CallMainMenu = view -> {
        Intent mScreen = new Intent(actScore.this,actMainMenu.class);
        startActivity(mScreen);
    };
}