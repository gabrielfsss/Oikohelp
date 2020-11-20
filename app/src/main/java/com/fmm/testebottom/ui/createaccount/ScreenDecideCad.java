package com.fmm.testebottom.ui.createaccount;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;

public class ScreenDecideCad extends AppCompatActivity {

    Button btnUser;
    Button btnPev;
    ImageButton btnAjuda;

    AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_decide_cad);

        btnPev = findViewById(R.id.btnPev);
        btnUser = findViewById(R.id.btnUsuario);
        btnAjuda = findViewById(R.id.ajudaCad);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user = new Intent(ScreenDecideCad.this, ScreenCadUser.class);
                startActivity(user);
            }
        });

        btnPev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pev = new Intent(ScreenDecideCad.this, ScreenCadPEV.class);
                startActivity(pev);
            }
        });

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAlertDialogAjuda();
            }
        });
    }

    private void handleAlertDialogAjuda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(R.layout.alert_ajuda_cad);

        alert = builder.create();
        alert.show();
    }

}
