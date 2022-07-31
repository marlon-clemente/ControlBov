package com.aplicativo.controlbov;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.String;
import java.util.Calendar;
import java.util.Random;

import data.Animal;
import data.Fotografia;

public class NovoAnimal extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnSalvar, btAdd;
    private TextView infSexo, infRegistro;
    private RadioGroup radioSexGroup;
    private RadioGroup radioIdade;
    private EditText txtDataNasc, txtRegistro, txtPeso;
    private Spinner dropdown_identificacao, dropdown_classificacao, dropdown_idade;

    //imagem do boi
    private ImageView imagemBoi;
    Integer REQUEST_CAMERA = 1, SELECT_FILE=0;
    private Uri mImageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    //
    private DatePickerDialog.OnDateSetListener dataNac;
    private final int PERMISSAO_REQUEST = 2, PERMISAO_IMG = 1;
    private final int uri = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_animal);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Novo Animal");

        carregaComponetes();

        identifica_animal();
        classifica_animal();
        seleciona_raca();


        imagemBoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagem();
                Toast.makeText(getApplicationContext(),"Clico na imagem", Toast.LENGTH_SHORT).show();
            }
        });

        RadioGroup rd_group = findViewById(R.id.GadoTipo);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();

        btnSalvar = (Button)findViewById(R.id.bSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                verificaCampos(id);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if (requestCode == PERMISSAO_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //deu certo
            } else {
                //negou
            }
            return;
        }
    }

    public void verificaCampos(String id){
        final String nome = txtRegistro.getText().toString();
        if (nome.isEmpty()){
            txtRegistro.setError("Por favor, informe um registro");
        } else {
            salvaBoi(id);
        }
    }

    public void salvaBoi (final String id){

        Random random = new Random();
        int idBoi = random.nextInt(10000000);

        final String nome = txtRegistro.getText().toString();
        final String tipo_registro = dropdown_identificacao.getSelectedItem().toString();
        String sexo = infSexo.getText().toString();
        String peso = txtPeso.getText().toString();
        String classificacao =dropdown_classificacao.getSelectedItem().toString();
        String nome_fotografia = id+"_"+tipo_registro+"_"+nome;
        String idade = dropdown_idade.getSelectedItem().toString();
        String idadeConv = idade.replaceAll("[^0-9.]", "");
        int idadeNum = Integer.parseInt(idadeConv);


        storageReference = FirebaseStorage.getInstance().getReference("animais/"+id+"/"+tipo_registro+"_"+nome);
        databaseReference = FirebaseDatabase.getInstance().getReference("Fotografia");

        if (mImageUri != null){
            storageReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri dowUri = task.getResult();
                        //Fotografia fotografia = new Fotografia(id+"_"+tipo_registro+"_"+nome, dowUri.toString());
                        //databaseReference.push().setValue(fotografia);
                    }
                }
            });
        }

        Animal boi = new Animal(nome,
                tipo_registro,
                sexo,
                "Corte",
                peso,
                classificacao,
                nome_fotografia,
                idadeNum);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Animal/"+id+"/"+idBoi);
        myRef.setValue(boi);

        finish();
    }

    public void seleciona_idade_c () {
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogo = new DatePickerDialog(NovoAnimal.this,
                                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                        dataNac,year,month,day);
                dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogo.show();
            }
        });
        dataNac = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDataNasc.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };
    }

    private void carregaComponetes(){
        radioSexGroup = (RadioGroup)findViewById(R.id.GadoTipo);
        txtRegistro = (EditText)findViewById(R.id.txtRegistro);
        txtDataNasc = (EditText)findViewById(R.id.boxDataNascimento);
        txtPeso = findViewById(R.id.txtPeso);
        //Dropdown
        dropdown_identificacao = findViewById(R.id.dp_identificador);
        dropdown_classificacao = findViewById(R.id.dp_classificacao);
        dropdown_idade = findViewById(R.id.dp_idade);
        //text
        infSexo = findViewById(R.id.defSexo);
        infRegistro = findViewById(R.id.defRegistro);
        //imagem
        imagemBoi = (ImageView) findViewById(R.id.imgBoi);
    }

    private void identifica_animal(){
        String[] itensCorte = new String[]{"Nome", "Número Manejo","Registro","Tatuagem"};
        ArrayAdapter<String> ListRacaCorte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itensCorte);
        dropdown_identificacao.setAdapter(ListRacaCorte);
        dropdown_identificacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tp = dropdown_identificacao.getSelectedItem().toString();
                infRegistro.setText(tp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void classifica_animal(){
        String[] itensClassificao = {"Selecione", "Novilha", "Bezerra","Vaca","Matriz","Bezerro","Garrote","Boi","Touro"};
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itensClassificao);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_classificacao.setAdapter(aa);
        dropdown_classificacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String cl = dropdown_classificacao.getSelectedItem().toString();
                    seleciona_sexo(cl);
                    seleciona_idade(cl);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void seleciona_sexo(String c){
        if (c == "Novilha" || c == "Bezerra" || c == "Vaca" || c == "Matriz"){
            infSexo.setText("Fêmea");
        } else if (c == "Selecione"){
            infSexo.setText(" ");
        } else {
            infSexo.setText("Macho");
        }
    }

    private void seleciona_idade(String c){
        //0 - 8 meses
        if (c == "Bezerro" || c == "Bezerra"){
            String[] idadeBezerro = {"0 meses", "1 meses","2 meses","3 meses","4 meses","5 meses","6 meses","7 meses","8 meses"};
            ArrayAdapter<String> ListBezerro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idadeBezerro);
            dropdown_idade.setAdapter(ListBezerro);
        } else if (c == "Novilha" || c == "Garrote"){
            String[] idadeN = {"9 meses", "10 meses","11 meses","12 meses","13 meses","14 meses","15 meses","16 meses","17 meses",
                                    "18 meses", "19 meses", "20 meses", "21 meses", "22 meses", "23 meses", "24 meses", "25 meses",
                                    "26 meses", "27 meses", "28 meses", "29 meses", "30 meses", "31 meses", "32 meses", "33 meses", "34 meses",
                                    "35 meses", "36 meses"};
            ArrayAdapter<String> ListNovilh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idadeN);
            dropdown_idade.setAdapter(ListNovilh);
        } else if (c == "Vaca"){
            String[] idadeVaca = {"25 meses ou mais"};
            ArrayAdapter<String> ListVaca = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idadeVaca);
            dropdown_idade.setAdapter(ListVaca);
        } else if(c == "Selecione"){
            String[] no = {" "};
            ArrayAdapter<String> noo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, no);
            dropdown_idade.setAdapter(noo);
        } else {
            String[] idader = {"37 meses ou mais"};
            ArrayAdapter<String> Listr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idader);
            dropdown_idade.setAdapter(Listr);
        }
    }

    private void seleciona_raca(){
        String[] idadeBezerro = {"" +
                "Nelore", "Cruzado","Gir","Guzerá","Simental","Aberdeen Anguss","Girolando","Holandês","Africander",
                "Aubrac","Ayrshire","Barzona","Beefalo","Beefmaster","Belgian Blue","Belmont Red","Blonde D'Aquitaine","Bonsmara",
                "Braford","Brahman","Brahmental","Braler","Brangus","Campino Red Pied","Canadienne","Canchim","Carabao","Caracu","Charbray",
                "Charolês","Chianina","Composto","Curraleiro","Danish Black & White", "Devon", "Dexter", "Dinamarquês Vermelho"};
        ArrayAdapter<String> ListBezerro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idadeBezerro);
        dropdown_idade.setAdapter(ListBezerro);
    }

    private void selectImagem(){
        final CharSequence[] items = {"Camera", "Galeria","Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NovoAnimal.this);
        builder.setTitle("Adicionar Imagem do Animal");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else if (items[i].equals("Galeria")){
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent2, "Selecione sua galeria"), SELECT_FILE);
                } else if (items[i].equals("Cancelar")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                imagemBoi.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE){
                mImageUri = data.getData();
                imagemBoi.setImageURI(mImageUri);
            }
        }

    }

}
