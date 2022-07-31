package com.aplicativo.controlbov;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import data.Animal;

public class MeuRebanho extends AppCompatActivity {
    private static final String TAG = "PostDetailActivity";
    private FirebaseAuth mAuth;
    FloatingActionButton addBoi;

    //componetes
    private TextView mTextMessage;
    private EditText boxPesquisar;
    private ProgressDialog progressDialog;
    //listas
    private ListView pesquisa;
    public ArrayList<Animal> listaAnimal  = new ArrayList<Animal>();
    private ArrayAdapter<Animal> arrayAdapteranimal;
    //firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_rebanho);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando seus dados...");
        progressDialog.show();
        iniciaComponentes();
        iniciaFirebase();
        //eventoEdit();
        Query query;
        query = databaseReference.child("Animal/1SWRYFvlbfUym4Fa0byVJCoN7Pw2").orderByChild("nome");

        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Animal a = objSnapshot.getValue(Animal.class);
                    listaAnimal.add(a);
                }
                arrayAdapteranimal = new AnimalAdapter(MeuRebanho.this, listaAnimal);
                pesquisa.setAdapter(arrayAdapteranimal);
                if (listaAnimal.isEmpty()){
                    Toast.makeText(MeuRebanho.this, "Lista está Vazia", Toast.LENGTH_SHORT);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });

        pesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MeuRebanho.this, ViewAnimal.class);
                intent.putExtra("boi", listaAnimal.get(position));
                startActivity(intent);

            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();

        addBoi = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        addBoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MeuRebanho.this, NovoAnimal.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener animalListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Animal animal = dataSnapshot.getValue(Animal.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(MeuRebanho.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        //database.addValueEventListener(animalListener);
    }
/*
    private void eventoEdit(){
        boxPesquisar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Palavra = boxPesquisar.getText().toString().trim();
                pesquisaPalavra(Palavra);
            }
        });
    }

    private void pesquisaPalavra(String palavra){
        Query query;
        if (palavra.equals("")){
            query = databaseReference.child("Animal/1SWRYFvlbfUym4Fa0byVJCoN7Pw2").orderByChild("nome");
        } else {
            query = databaseReference.child("Animal/1SWRYFvlbfUym4Fa0byVJCoN7Pw2").orderByChild("nome")
                    .startAt(palavra).endAt(palavra+"\uf8ff");
        }

        listaAnimal.clear();

        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Animal a = objSnapshot.getValue(Animal.class);
                    listaAnimal.add(a);
                }
                arrayAdapteranimal = new ArrayAdapter<Animal>(MeuRebanho.this,
                        android.R.layout.simple_list_item_1, listaAnimal);
                pesquisa.setAdapter(arrayAdapteranimal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });
    }
*/
    private void iniciaComponentes(){
        //boxPesquisar = findViewById(R.id.boxPesquisa);
        pesquisa = findViewById(R.id.listPesquisa);
    }

    private void iniciaFirebase(){
        FirebaseApp.initializeApp(MeuRebanho.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        pesquisaPalavra(" ");
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rebanho, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_nome:
                Toast.makeText(this, "Item 1", Toast.LENGTH_SHORT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
