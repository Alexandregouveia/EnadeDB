package com.example.alexandre.enadedb;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;
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
import java.net.URI;
import java.net.URL;
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

    Historico hist = null;
    Usuario usuario;
    ArrayList<Historico> listH;
    File photo;
    Bitmap img;
    int grupo;
    String curso;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        listH = new ArrayList<>();
        name = findViewById(R.id.MainName);
        lastName = findViewById(R.id.MainLastName);
        photoProfile = findViewById(R.id.MainPhoto);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {


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




        try{
            hist = getIntent().getExtras().getParcelable("atual");
        }catch (Exception ex){}

        if (hist!=null){
            listH.add(hist);
            FirebaseDatabase.getInstance().getReference("historico").child(mUser.getUid()).
                    addValueEventListener(new ValueEventListener() {
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

        }else{
            FirebaseDatabase.getInstance().getReference("historico").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
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
        }


        try{
            photo = File.createTempFile("photo","jpg");
        }catch (Exception ex){}

        FirebaseStorage.getInstance().getReference("photos/"+mUser.getUid()+"/enadedb.jpg")
                .getFile(photo)
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
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("historico").child(mUser.getUid()).setValue(listH);
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


            ArrayList<CharSequence> anos = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("provas").child(usuario.getCurso()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        anos.add(child.getKey());
                    }
                    ArrayAdapter<CharSequence> spinnerArrayAdapter = new ArrayAdapter<>(
                            getApplicationContext(), android.R.layout.simple_spinner_item, anos);
                    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    spAno.setAdapter(spinnerArrayAdapter);
                    Log.d("chaves", String.valueOf(anos.size()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


//############################## Visualizar Gabarito ###############################################
        } else if (id == R.id.nav_gabarito) { // Visualizar Gabarito
            fpScreen.setDisplayedChild(1);
            Visualizar = findViewById(R.id.IniciarRes);
            Visualizar.setText("visualizar");
            Visualizar.setOnClickListener(CallGabarito);

            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Conferir Gabarito");

            spAno = findViewById(R.id.spTestyear);
            ArrayAdapter<CharSequence> listaAno;

            //Define em quais anos teve prova
            ArrayList<CharSequence> anos = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("gabarito").child(usuario.getCurso()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        anos.add(child.getKey());
                    }
                    ArrayAdapter<CharSequence> spinnerArrayAdapter = new ArrayAdapter<>(
                            getApplicationContext(), android.R.layout.simple_spinner_item, anos);
                    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    spAno.setAdapter(spinnerArrayAdapter);
                    Log.d("chaves", String.valueOf(anos.size()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//################################ Baixar PDF ######################################################
        } else if (id == R.id.nav_pdf) { //Baixar Pdf
            fpScreen.setDisplayedChild(1);

            int permissionStorage = ContextCompat.checkSelfPermission(actMainMenu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStorage== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(actMainMenu.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
            }
            Visualizar = findViewById(R.id.IniciarRes);
            Visualizar.setOnClickListener(DownloadPdf);
            Titulo = findViewById(R.id.Titulo);
            Titulo.setText("Download do pdf");
            Visualizar.setText("Baixar");

            spAno = findViewById(R.id.spTestyear);

            //Define em quais anos teve prova

            FirebaseDatabase.getInstance().getReference("PDF").child(usuario.getCurso()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String anos = dataSnapshot.getValue(String.class);
                    String[] listAnos = anos.split("#");
                    ArrayAdapter<CharSequence> spinnerArrayAdapter = new ArrayAdapter<>(
                            getApplicationContext(), android.R.layout.simple_spinner_item, listAnos);
                    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    spAno.setAdapter(spinnerArrayAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


//####################### Atualizar dados pessoais##################################################
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

    View.OnClickListener DownloadPdf = view -> {
        Log.d("Selecionado", spAno.getSelectedItem().toString());
        FirebaseStorage.getInstance().getReference(usuario.getCurso())
                .child(spAno.getSelectedItem().toString())
                .child("prova.pdf")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        Uri downUri = uri;
                        DownloadManager.Request request = new DownloadManager.Request(downUri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setAllowedOverRoaming(false);
                        request.setTitle(usuario.getCurso()+"-"+spAno.getSelectedItem().toString());
                        request.setDescription("Downloading " + usuario.getCurso()+"-"+spAno.getSelectedItem().toString());
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, usuario.getCurso()+"-"+spAno.getSelectedItem().toString()+".pdf");
                        downloadManager.enqueue(request);


                    }
                });
    };


}
