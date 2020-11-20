package com.fmm.testebottom.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;
import com.fmm.testebottom.ui.createaccount.ScreenDecideCad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ScreenLogin extends AppCompatActivity {

    Button btnLogin;
    TextView btnCad;
    EditText txtEmail, txtPass;
    private FirebaseAuth mAuth;
    AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        mAuth = FirebaseAuth.getInstance();

        btnCad = findViewById(R.id.newCad);
        btnLogin = findViewById(R.id.button);

        txtEmail = (EditText) findViewById(R.id.password);
        txtPass = (EditText)findViewById(R.id.email);

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent decideCad = new Intent(ScreenLogin.this, ScreenDecideCad.class);
                startActivity(decideCad);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(txtEmail.length() != 0 && txtPass.length() != 0){
                   mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(),txtPass.getText().toString())
                           .addOnCompleteListener(ScreenLogin.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful()){
                                       handleLogin();
                                   }else{
                                        handleAlertDialog("Email ou senha inválidos !");
                                   }
                               }
                           });
               }else{
                    handleAlertDialog("Os campos devem estar preenchidos !");
               }
            }
        });
    }

    private void handleLogin(){
        Intent home = new Intent(ScreenLogin.this, Home.class);
        startActivity(home);
    }

    private void handleAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
