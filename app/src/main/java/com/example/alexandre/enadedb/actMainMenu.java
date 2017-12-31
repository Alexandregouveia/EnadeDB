package com.example.alexandre.enadedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.BaseAdapter;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.valueOf;

public class actMainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper fpScreen;
    Button responder;
    Button Visualizar;
    TextView Titulo;
    Spinner spAno;
    ArrayList<Historico> listH = new ArrayList<>();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        fpScreen = findViewById(R.id.flipperv);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showPopUp();

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


//############################### Resolver questões ################################################
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

//############################## VIsualizar Gabarito ###############################################
        } else if (id == R.id.nav_gabarito) { // Visualizar Gabarito
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            //Visualizar.setOnClickListener(CallGabarito);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Conferir Gabarito");
            Visualizar.setText("visualizar");
            Visualizar.setOnClickListener(CallGabarito);

            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                    R.array.grupo_1,
                    android.R.layout.simple_spinner_item);
            listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAno.setAdapter(listaAno);

//################################ Baixar PDF ######################################################
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


        } else if (id == R.id.nav_logout){ //Logout
            mAuth.signOut();
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


    static class ViewHolder{
        TextView score;
        TextView date;
        TextView testDate;
    }

    public class cardAdapter extends BaseAdapter{
        private ArrayList<Historico> listaHist = listH;
        private LayoutInflater layoutInflater;

        public cardAdapter(Context context, ArrayList<Historico> arraylist){
            listaHist = arraylist;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return listaHist.size();
        }

        @Override
        public Historico getItem(int i){
            return listaHist.get(i);
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            ViewHolder viewHolder;

            if (view==null){
                viewHolder = new ViewHolder();

                view = layoutInflater.inflate(R.layout.historic_card, parent, false);
                viewHolder.score = view.findViewById(R.id.hist_score);
                viewHolder.date = view.findViewById(R.id.hist_date);
                viewHolder.testDate = view.findViewById(R.id.hist_test_date);
                view.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) view.getTag();
            }


            Historico hist = getItem(position);
            if (hist!=null){
                viewHolder.testDate.setText(getString(R.string.card1) +" "+ valueOf(hist.getAno()));
                viewHolder.date.setText(getString(R.string.card2)+" "+ hist.getTest_data());
                if (hist.getScore()<50){
                    viewHolder.score.setTextColor(getColor(R.color.low));
                }else if (hist.getScore()>=50 && hist.getScore()<70){
                    viewHolder.score.setTextColor(getColor(R.color.medium));
                }else {
                    viewHolder.score.setTextColor(getColor(R.color.high));
                }
                viewHolder.score.setText(" "+valueOf(hist.getScore())+" ");

            }

            return view;
        }



    }
    public void showPopUp(){
        int numberItems = 10;
        ArrayList<Historico> hist= new ArrayList<>();

        for (int x = 0; x<numberItems; x++){
            hist.add(new Historico(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()),60,2005));
        }

        ListView listaH = findViewById(R.id.ListHistoric);
        listaH.setAdapter(new cardAdapter(actMainMenu.this,hist));

    }

    View.OnClickListener CallGabarito = view -> {
      Intent gab = new Intent(actMainMenu.this, actGabarito.class);
      gab.putExtra("curso","Bach Ciência da Computação");
      gab.putExtra("ano",spAno.getSelectedItem().toString());
      startActivity(gab);
    };

}
