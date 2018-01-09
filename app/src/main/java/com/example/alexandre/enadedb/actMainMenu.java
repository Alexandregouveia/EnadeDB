package com.example.alexandre.enadedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.BaseAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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
    TextView name;
    TextView lastName;
    ImageView photoProfile;

    Usuario usuario;
    ArrayList<Historico> listH = new ArrayList<>();
    File photo;
    Bitmap img;
    int grupo;
    String curso;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRefH;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.MainName);
        lastName = findViewById(R.id.MainLastName);
        photoProfile = findViewById(R.id.MainPhoto);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                name.setText(usuario.getName());
                lastName.setText(usuario.getLastName());
                grupo = usuario.getGrupo();
                curso = usuario.getCurso();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mRefH = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).child("historico");
        mRefH.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Historico hist = child.getValue(Historico.class);
                    listH.add(hist);
                }
                showPopUp(listH);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try{
            photo = File.createTempFile("photo","jpg");
        }catch (Exception ex){}

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference("photos/"+mUser.getUid()+"/enadedb.jpg");
        mStorageRef.getFile(photo)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        img = BitmapFactory.decodeFile(photo.toString());
                        photoProfile.setImageBitmap(img);
                    }
                });

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


//############################### Resolver questões ################################################
        if (id == R.id.nav_resol) { // Resolucao de questoes
            fpScreen.setDisplayedChild(1);

            responder = findViewById(R.id.IniciarRes);
            responder.setOnClickListener(StartQuest);
            responder.setText(R.string.iniciar);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText(R.string.title);


            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno;

            //Define em quais anos ocorreu
            switch (grupo){
                case 1:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_1,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 2:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_2,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 3:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_2,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
            }



//############################## Visualizar Gabarito ###############################################
        } else if (id == R.id.nav_gabarito) { // Visualizar Gabarito
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            //Visualizar.setOnClickListener(CallGabarito);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Conferir Gabarito");
            Visualizar.setText("visualizar");
            Visualizar.setOnClickListener(CallGabarito);

            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno;
            //Define em quais anos tive prova
            switch (grupo){
                case 1:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_1,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 2:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_2,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 3:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_3,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
            }

//################################ Baixar PDF ######################################################
        } else if (id == R.id.nav_pdf) { //Baixar Pdf
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            //Visualizar.setOnClickListener(DownloadPdf);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Download do pdf");
            Visualizar.setText("Baixar");

            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno;
            //Define em quais anos tive prova
            switch (grupo){
                case 1:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_1,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 2:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_2,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
                case 3:
                    listaAno = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.grupo_3,
                            android.R.layout.simple_spinner_item);
                    listaAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAno.setAdapter(listaAno);
                    break;
            }

        } else if (id == R.id.nav_atualizar) { //Atualizar Dados pessoais
            Intent Att = new Intent(actMainMenu.this, actCadastro.class);
            Att.putExtra("update",1);
            startActivity(Att);

        } else if (id == R.id.nav_historico) { //Hitorico de partidas do usuario
            fpScreen.setDisplayedChild(0);

//############################### Sair #############################################################
        } else if (id == R.id.nav_logout){ //Logout
            mAuth.signOut();
            Intent logOut = new Intent(actMainMenu.this, MainActivity.class);
            startActivity(logOut);
        }

//##################################################################################################
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




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
    public void showPopUp(ArrayList<Historico> lh){
//        int numberItems = 10;
//        ArrayList<Historico> hist= new ArrayList<>();
//
//        for (int x = 0; x<numberItems; x++){
//            hist.add(new Historico(new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime()),70,2005));
//        }

        ListView listaH = findViewById(R.id.ListHistoric);
        listaH.setAdapter(new cardAdapter(actMainMenu.this,lh));

    }

    View.OnClickListener CallGabarito = view -> {
      Intent gab = new Intent(actMainMenu.this, actGabarito.class);
      gab.putExtra("curso",usuario.getCurso());
      gab.putExtra("ano",spAno.getSelectedItem().toString());
      startActivity(gab);
    };

    View.OnClickListener StartQuest = view -> {
        Intent quest = new Intent(this, actQuestao.class);
        quest.putExtra("curso",usuario.getCurso());
        quest.putExtra("ano",spAno.getSelectedItem().toString());
        startActivity(quest);
    };

}
