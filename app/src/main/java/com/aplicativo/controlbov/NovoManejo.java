package com.aplicativo.controlbov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import data.Animal;

public class NovoManejo extends AppCompatActivity {

    private Spinner d_tipo_manejo, d_info_manejo;
    private Button btSalvar;
    private TextView infoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_manejo);
        inicia_compontentes();

        final Animal animal = (Animal) getIntent().getSerializableExtra("animal_manejo");

        seleciona_manejo(animal);
    }

    public void inicia_compontentes(){
        d_tipo_manejo = findViewById(R.id.dp_tipo_manejo);

        d_info_manejo = findViewById(R.id.dp_info_manejo);
        d_info_manejo.setVisibility(View.INVISIBLE);

        btSalvar = findViewById(R.id.nm_btSalvar);
        btSalvar.setVisibility(View.INVISIBLE);

        infoTxt = findViewById(R.id.txtAlerta);
        infoTxt.setVisibility(View.INVISIBLE);

    }

    public void seleciona_manejo(final Animal a){
        String[] itensClassificao = {"Selecione um manejo","Vacina"};
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itensClassificao);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_tipo_manejo.setAdapter(aa);
        d_tipo_manejo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cl = d_tipo_manejo.getSelectedItem().toString();
                if (cl == "Selecione um manejo"){
                    btSalvar.setVisibility(View.INVISIBLE);
                    d_info_manejo.setVisibility(View.INVISIBLE);
                } else if (cl == "Vacina"){
                    btSalvar.setVisibility(View.VISIBLE);
                    d_info_manejo.setVisibility(View.VISIBLE);
                    seleciona_vacina(a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void mostraInfo(String txt){
        infoTxt.setVisibility(View.VISIBLE);
        infoTxt.setText(txt);
    }

    public void seleciona_vacina(final Animal a){
        String[] itensv= {"Selecione uma vacina","Aftosa"};
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itensv);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_info_manejo.setAdapter(aa);
        d_info_manejo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cl = d_tipo_manejo.getSelectedItem().toString();
                if (cl == "Afetosa"){
                    if (a.getIdade() == 1){
                        mostraInfo("O animal ainda não alcançou os 4 meses de idade! Idade " +
                                "não recomendada para a aplicação da vacina!");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
