package com.example.alexandre.enadedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class actMainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper fpScreen;
    Button responder;
    Button Visualizar;
    TextView Titulo;
    Spinner spAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fpScreen = findViewById(R.id.flipperv);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fpScreen.setDisplayedChild(0);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_resol) { // Resolucao de questoes
            fpScreen.setDisplayedChild(1);

            responder = findViewById(R.id.IniciarRes);
            responder.setOnClickListener(StartQuest);

            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                    R.array.grupo_1,
                    android.R.layout.simple_spinner_item);
            listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAno.setAdapter(listaAno);

        } else if (id == R.id.nav_gabarito) { // Visualizar Gabarito
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            //Visualizar.setOnClickListener(CallGabarito);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Conferir Gabarito");
            Visualizar.setText("visualizar");

        } else if (id == R.id.nav_pdf) { //Baixar Pdf
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            //Visualizar.setOnClickListener(DownloadPdf);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Download do pdf");
            Visualizar.setText("Baixar");

        } else if (id == R.id.nav_atualizar) { //Atualizar Dados pessoais
            Intent Att = new Intent(actMainMenu.this, actCadastro.class);
            Att.putExtra("update",1);
            startActivity(Att);

        } else if (id == R.id.nav_historico) { //Hitorico de partidas do usuario
            fpScreen.setDisplayedChild(0);
            // TODO: 22/11/17 popular listview

        } else if (id == R.id.nav_logout){ //Logout
            Intent logOut = new Intent(actMainMenu.this, MainActivity.class);
            startActivity(logOut);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener StartQuest = view -> {
        Intent quest = new Intent(this, actQuestao.class);
        startActivity(quest);
    };

}
