package com.fmm.testebottom.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.Pev;
import com.fmm.testebottom.models.User;
import com.fmm.testebottom.ui.ScreenLogin;
import com.fmm.testebottom.ui.UpdateCads.UpdateCadPev;
import com.fmm.testebottom.ui.UpdateCads.UpdateCadUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class AccountFragment extends Fragment {

    String[] items = {"Atualizar Dados", "Sair"};

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference myRef;

    AlertDialog alert;
    ImageButton options;

    Pev pevData;
    User userData;

    TextView txt_account_name;
    TextView txt_account_contato;
    TextView txt_account_email;
    TextView txt_account_cpf;
    TextView txt_account_materiais;

    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();

        View view = inflater.inflate(R.layout.screen_account, container, false);

        try {
            myRef = db.getReference("users");
            myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        userData = snapshot.getValue(User.class);
                        setDataUser(userData);
                    }else {
                        myRef = db.getReference("pevs");
                        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                pevData = snapshot.getValue(Pev.class);
                                setDataPev(pevData);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception ex){
           Log.d("data", ex.getMessage());
        }

        options = view.findViewById(R.id.menuOptions);
        txt_account_name = view.findViewById(R.id.user_name);
        txt_account_contato = view.findViewById(R.id.user_contact);
        txt_account_email = view.findViewById(R.id.user_email);
        txt_account_materiais = view.findViewById(R.id.txt_materiais);

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                handleScreenUpdate();
                                break;

                            case 1:
                                handleAlertDialogLogof("Deseja sair da sua conta ?");
                                break;
                        }
                    }
                });
                alert = builder.create();
                alert.show();
            }
        });
    }

    private void handleAlertDialogLogof(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Fechar");
        builder.setMessage(message);
        builder.setNegativeButton(R.string.button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), ScreenLogin.class);
                startActivity(intent);
            }
        });

        alert = builder.create();
        alert.show();
    }

    private void handleScreenUpdate(){
        if(pevData != null){
            Intent update = new Intent(getContext(), UpdateCadPev.class);
            startActivity(update);
        }else if(userData != null){
            Intent update = new Intent(getContext(), UpdateCadUser.class);
            startActivity(update);
        }

    }

    private void setDataUser(User user){
        txt_account_name.setText("Nome: " + user.getNome());
        txt_account_contato.setText("Contato: " +user.getContato());
        txt_account_email.setText("Email: " + user.getEmail());
    }

    private void setDataPev(Pev pev){
        txt_account_name.setText("Nome: " + pev.getNome());
        txt_account_contato.setText("Contato: " +pev.getContato());
        txt_account_email.setText("Email: " + pev.getEmail());

        Iterator it = pev.getMateriais().iterator();
        String materiais = " Materias coletados: ";
        while (it.hasNext()){
            materiais += it.next() + ",";
        }
        txt_account_materiais.setText(materiais.substring(0, materiais.length() - 1));

    }
}