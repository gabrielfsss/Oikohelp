package com.fmm.testebottom.ui.createaccount;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.User;
import com.fmm.testebottom.ui.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScreenCadUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    private EditText edUsuario;
    private EditText edContato;
    private EditText edEmail;
    private EditText edSenha;

    private Button btnConfirmar;

    AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_cad_user);

        edUsuario = findViewById(R.id.edUsuario);
        edContato = findViewById(R.id.edContato);
        edEmail = findViewById(R.id.edEmail);
        edSenha = findViewById(R.id.edSenha);

        btnConfirmar = findViewById(R.id.confirmCad);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("users");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enabledFields(false);
                if (edContato.getText().length() < 1 || edUsuario.getText().length() < 1 || edEmail.getText().length() < 1 || edSenha.getText().length() < 1) {
                    handleAlertDialog("Todos os campos devem estar preenchidos.");
                    enabledFields(true);
                }else{
                    final User user = new User(edUsuario.getText().toString(),
                            edContato.getText().toString(),
                            edEmail.getText().toString(),
                            edSenha.getText().toString(),
                            0);

                    mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            user.setId(authResult.getUser().getUid());
                            myRef.child(user.getId()).setValue(user);

                            handleAlertDialog("Cadastro realizado com sucesso !");
                            handleNavigation();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           handleAlertDialog(e.getMessage());
                           enabledFields(true);
                        }
                    });
                }
            }
        });
    }

    private void enabledFields(boolean state){
        edSenha.setEnabled(state);
        edEmail.setEnabled(state);
        edContato.setEnabled(state);
        edUsuario.setEnabled(state);
        btnConfirmar.setEnabled(state);
    }

    private void handleAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);

        alert = builder.create();
        alert.show();
    }

    private void handleNavigation(){
        Intent home = new Intent(ScreenCadUser.this, Home.class);
        startActivity(home);
    }
}
