package com.fmm.testebottom.ui.createaccount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.Localization;
import com.fmm.testebottom.models.Pev;
import com.fmm.testebottom.ui.Home;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScreenCadPEV extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    private EditText edPevNome;
    private EditText edPevContato;
    private EditText edPevSenha;
    private EditText edPevEmail;

    private CheckBox checkEletronicos,checkPilhasBateria,checkLampada,checkROrganico,checkOleoCosinha,checkPapelPapelao;


    private ImageButton btnGetLocation;
    private Button btnConfirmCad;

    private TextView selectLocation;

    AlertDialog alert;

    private FusedLocationProviderClient myLocation;
    private LatLng myPosition;

    private boolean isSelectlocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_cad_pev);

        edPevNome = findViewById(R.id.edPevNome);
        edPevContato = findViewById(R.id.edPevContato);
        edPevSenha = findViewById(R.id.edPevSenha);
        edPevEmail = findViewById(R.id.edPevEmail);

        checkEletronicos = findViewById(R.id.checkReciduosEletronicos);
        checkPilhasBateria = findViewById(R.id.checkPilhasBateria);
        checkLampada = findViewById(R.id.checkLampada);
        checkROrganico = findViewById(R.id.checkResiduosOrganicos);
        checkOleoCosinha = findViewById(R.id.checkOleodeCozinha);
        checkPapelPapelao = findViewById(R.id.PapelPapelao);

        btnConfirmCad = findViewById(R.id.confirmCad);
        btnGetLocation = findViewById(R.id.btnSelcionaLocalizacao);

        selectLocation = findViewById(R.id.textSelecionaLocalizacao);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("pevs");
        mAuth = FirebaseAuth.getInstance();

        myLocation = LocationServices.getFusedLocationProviderClient(this);

        isSelectlocation = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnConfirmCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnabledFields(false);
                if (edPevEmail.getText().length() < 1 || edPevNome.getText().length() < 1 || edPevContato.getText().length() < 1 || edPevSenha.getText().length() < 1 || !checkIsSelected() ) {
                    handleAlertDialog("Todos os campos devem estar preenchidos!");
                    setEnabledFields(true);
                } else {

                    if (myPosition != null) {
                        mAuth.createUserWithEmailAndPassword(edPevEmail.getText().toString(), edPevSenha.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Pev pev = new Pev(
                                                edPevNome.getText().toString(),
                                                edPevContato.getText().toString(),
                                                edPevEmail.getText().toString(),
                                                new Localization(myPosition.latitude, myPosition.longitude),
                                                joinMaterials(),
                                                1);

                                        myRef.child(authResult.getUser().getUid())
                                                .setValue(pev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    handleAlertDialog("Cadastro realizado com sucesso!");
                                                    handleNavigation();
                                                }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                handleAlertDialog("Erro ao realizar o seu cadastro! Por favor, tente novamente.");
                                                setEnabledFields(true);
                                            }
                                        });
                                    }
                                });
                    } else {
                        handleAlertDialog("Selecione a sua localização");
                        setEnabledFields(true);
                    }
                }
            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

    }

    private boolean checkIsSelected(){
        if (checkLampada.isChecked() || checkPapelPapelao.isChecked() || checkOleoCosinha.isChecked()|| checkROrganico.isChecked() || checkPilhasBateria.isChecked()|| checkEletronicos.isChecked()) {
            return true;
        }else{
            return false;
        }
    }

    private ArrayList<String> joinMaterials() {

        ArrayList materials = new ArrayList<String>();

        if (checkPapelPapelao.isChecked()) {
            materials.add(checkPapelPapelao.getText().toString());
        }

        if (checkOleoCosinha.isChecked()) {
            materials.add(checkOleoCosinha.getText().toString());
        }

        if (checkROrganico.isChecked()) {
            materials.add(checkROrganico.getText().toString());
        }
        if (checkEletronicos.isChecked()) {
            materials.add(checkEletronicos.getText().toString());
        }
        if (checkLampada.isChecked()) {
            materials.add(checkLampada.getText().toString());
        }
        if (checkPilhasBateria.isChecked()) {
            materials.add(checkPilhasBateria.getText().toString());
        }

        return materials;
    }

    private void setEnabledFields(boolean state) {
        edPevSenha.setEnabled(state);
        edPevContato.setEnabled(state);
        edPevNome.setEnabled(state);
        edPevEmail.setEnabled(state);
        btnConfirmCad.setEnabled(state);
        btnGetLocation.setEnabled(state);
        checkLampada.setEnabled(state);
        checkPilhasBateria.setEnabled(state);
        checkEletronicos.setEnabled(state);
        checkROrganico.setEnabled(state);
        checkOleoCosinha.setEnabled(state);
        checkPapelPapelao.setEnabled(state);

    }

    private void handleAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);

        alert = builder.create();
        alert.show();
    }

    private void handleNavigation() {
        Intent home = new Intent(ScreenCadPEV.this, Home.class);
        startActivity(home);
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
                    myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    selectLocation.setText("Localização selecionada");
                }
            }
        });
    }
}
