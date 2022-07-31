package com.aplicativo.controlbov;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import data.Animal;

public class AnimalAdapter extends ArrayAdapter<Animal> {

    private final Context context;
    private final ArrayList<Animal> animals;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

    public AnimalAdapter(Context context, ArrayList<Animal> animals){
        super(context, R.layout.elemento_animal, animals);
        this.context = context;
        this.animals = animals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.elemento_animal, parent, false);
        final ImageView img = rowView.findViewById(R.id.imagemAnimal);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("animais/"+id+"/"+animals.get(position).getTipo_registro()
                +"_"+animals.get(position).getNome_registro());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .into(img);
            }
        });

        TextView tipo = rowView.findViewById(R.id.txtTipoRegistro);
        TextView nome = rowView.findViewById(R.id.txtNomeRegistro);
        TextView idade = rowView.findViewById(R.id.txtIdade);
        TextView tamanho = rowView.findViewById(R.id.txtTamanho);



        Glide.with(context)
                .load(storageReference)
                .into(img);

        tipo.setText(animals.get(position).getTipo_registro());
        nome.setText(animals.get(position).getNome_registro());
        tamanho.setText(animals.get(position).getClassificacao());
        //tamanho.setText(animals.get(position).get());

        return rowView;
    }

}
