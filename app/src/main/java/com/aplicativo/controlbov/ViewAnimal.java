package com.aplicativo.controlbov;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import data.Animal;

public class ViewAnimal extends AppCompatActivity {

    private ImageView imgBoi;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Animal animal = (Animal) getIntent().getSerializableExtra("boi");
        String nome = animal.getNome_registro().toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nome);

        iniciaComponentes();
        mostra_imagem(animal);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAnimal.this, NovoManejo.class);
                intent.putExtra("animal_manejo", animal);
                startActivity(intent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });


    }

    public void iniciaComponentes(){
        imgBoi = (ImageView)findViewById(R.id.ava_imgBoi);
    }

    public void mostra_imagem(Animal animal){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("animais/"+id+"/"+animal.getTipo_registro()
                +"_"+animal.getNome_registro());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ViewAnimal.this)
                        .load(uri.toString())
                        .into(imgBoi);
            }
        });
    }
}
