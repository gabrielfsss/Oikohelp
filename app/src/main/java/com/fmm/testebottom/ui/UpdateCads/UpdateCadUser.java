package com.fmm.testebottom.ui.UpdateCads;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.User;
import com.fmm.testebottom.ui.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateCadUser extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseUser user;
    private User userData;
    private EditText upUsuario;
    private EditText upContato;
    private Button upAtualizaCad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_edit_user);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        upUsuario = (EditText) findViewById(R.id.upUsuario);
        upContato = (EditText) findViewById(R.id.upContato);
        upAtualizaCad = (Button) findViewById(R.id.updateCad);
        upAtualizaCad.setEnabled(false);
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        upAtualizaCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCad();
            }
        });

        upUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!upUsuario.getText().toString().equals(userData.getNome())){
                    userData.setNome(upUsuario.getText().toString());
                    upAtualizaCad.setEnabled(true);
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
                if(!upContato.getText().toString().equals(userData.getContato())){
                    userData.setContato(upContato.getText().toString());
                    upAtualizaCad.setEnabled(true);
                }
            }
        });

    }

    private void getData(){
        if (user != null) {
            myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        userData = snapshot.getValue(User.class);

                        upUsuario.setText(userData.getNome());
                        upContato.setText(userData.getContato());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void updateCad(){
        if (user != null){
            upAtualizaCad.setEnabled(false);
            myRef.child(user.getUid()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Dados atualizados!", Toast.LENGTH_LONG).show();
                    Intent prev = new Intent(UpdateCadUser.this, Home.class);
                    startActivity(prev);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Falha ao atualizar os dados!", Toast.LENGTH_LONG).show();
                    upAtualizaCad.setEnabled(true);
                }
            });
        }
    }
}
