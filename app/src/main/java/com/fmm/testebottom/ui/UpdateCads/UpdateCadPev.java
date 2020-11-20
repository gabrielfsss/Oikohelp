package com.fmm.testebottom.ui.UpdateCads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.Localization;
import com.fmm.testebottom.models.Pev;
import com.fmm.testebottom.ui.Home;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateCadPev extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private FusedLocationProviderClient myLocation;

    private Pev pev;
    private Localization myLocal;
    private ArrayList<String> materials;

    private EditText upNome;
    private EditText upContato;



    private CheckBox checkEletronicos,checkPilhasBateria,checkLampada,checkROrganico,checkOleoCosinha,checkPapelPapelao;

    private ImageButton upLocalizaçao;
    private Button upCadPev;
    private TextView txtViewLocalization;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_edit_pev);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("pevs");
        user = FirebaseAuth.getInstance().getCurrentUser();

        upNome = (EditText) findViewById(R.id.upPevNome);
        upContato = (EditText) findViewById(R.id.upPevContato);
       // upEmail = (EditText) findViewById(R.id.upPevEmail);

        checkEletronicos = findViewById(R.id.upCheckREletronico);
        checkPilhasBateria = findViewById(R.id.upCheckPilhaBateria);
        checkLampada = findViewById(R.id.upCheckLampada);
        checkROrganico = findViewById(R.id.upCheckROrganicos);
        checkOleoCosinha = findViewById(R.id.upCheckOleoCosinha);
        checkPapelPapelao = findViewById(R.id.upCheckPapelPapelao);

        upLocalizaçao = (ImageButton) findViewById(R.id.upLocalizacao);
        upCadPev = (Button) findViewById(R.id.updateCadPev);
        txtViewLocalization = (TextView) findViewById(R.id.textSelecionaLocalizacao);

        myLocation = LocationServices.getFusedLocationProviderClient(this);

        upCadPev.setEnabled(true);

        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        upCadPev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        upNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!upNome.getText().toString().equals(pev.getNome())){
                    pev.setNome(upNome.getText().toString());
                    upCadPev.setEnabled(true);
                }
            }
        });
        upContato.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!upContato.getText().toString().equals(pev.getContato())){
                    pev.setContato(upContato.getText().toString());
                    upCadPev.setEnabled(true);
                }
            }
        });

        checkLampada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Lampada")){
                        materials.add("Lampada");
                    }
                }else{
                    materials.remove("Lampada");
                }
                upCadPev.setEnabled(true);
            }
        });
        checkOleoCosinha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Óleo de cozinha")){
                        materials.add("Óleo de cozinha");
                    }
                }else{
                    materials.remove("Óleo de cozinha");
                }
                upCadPev.setEnabled(true);
            }
        });
        checkEletronicos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Residuos Eletrônicos")){
                        materials.add("Residuos Eletrônicos");
                    }
                }else{
                    materials.remove("Residuos Eletrônicos");
                }
                upCadPev.setEnabled(true);
            }
        });

        checkROrganico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Residuos Orgânicos")){
                        materials.add("Residuos Orgânicos");
                    }
                }else{
                    materials.remove("Residuos Orgânicos");
                }
                upCadPev.setEnabled(true);
            }
        });
        checkPilhasBateria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Pilhas e Bateria")){
                        materials.add("Pilhas e Bateria");
                    }
                }else{
                    materials.remove("Pilhas e Bateria");
                }
                upCadPev.setEnabled(true);
            }
        });
        checkPapelPapelao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!materials.contains("Papel e Papelão")){
                        materials.add("Papel e Papelão");
                    }
                }else{
                    materials.remove("Papel e Papelão");
                }
                upCadPev.setEnabled(true);
            }
        });

        upLocalizaçao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
    }

    private void getData(){
        if(user != null){
            myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        pev = snapshot.getValue(Pev.class);
                        upNome.setText(pev.getNome());
                        upContato.setText(pev.getContato());
                        materials = pev.getMateriais();
                        setCheckMaterials(pev.getMateriais());
                        myLocal = pev.getLocalizacao();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setCheckMaterials(ArrayList<String> material){
        for(int i = 0; i < material.size(); i++){
            switch (material.get(i)){
                case "Residuos orgânicos":
                        checkPilhasBateria.setChecked(true);
                    break;

                case "Residuos eletrônicos":
                        checkEletronicos.setChecked(true);
                    break;

                case "Papel e Papelão":
                        checkPapelPapelao.setChecked(true);
                    break;
                case "Óleo de cozinha":
                    checkOleoCosinha.setChecked(true);
                    break;
                case "Pilhas ou Bateria":
                    checkPilhasBateria.setChecked(true);
                    break;
                case "Lampadas":
                    checkLampada.setChecked(true);
                    break;
            }
        }
    }

    private void updateData(){
        if(user != null){
            upCadPev.setEnabled(false);
            myRef.child(user.getUid()).setValue(pev)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Dados atualizados!", Toast.LENGTH_LONG).show();
                            Intent prev = new Intent(UpdateCadPev.this, Home.class);
                            startActivity(prev);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Falha ao atualizar os dados!", Toast.LENGTH_LONG).show();
                            upCadPev.setEnabled(true);
                        }
                    });

        }
    }

    private void getMyLocation() {
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getMyLocationNow();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getMyLocationNow();
            }else {
                Toast.makeText(this, "Acesso ao local não permitido!", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocationNow(){
        myLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    myLocal = new Localization(location.getLatitude(), location.getLongitude());
                    txtViewLocalization.setText("Localização Atualizada");
                    upCadPev.setEnabled(true);
                }
            }
        });
    }
}
