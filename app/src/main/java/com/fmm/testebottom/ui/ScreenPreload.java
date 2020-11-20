package com.fmm.testebottom.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScreenPreload extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_preload);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent home = new Intent(ScreenPreload.this, Home.class);
            startActivity(home);
        }else{
            Intent login = new Intent(ScreenPreload.this, ScreenLogin.class);
            startActivity(login);
        }
    }
}
