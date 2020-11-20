package com.fmm.testebottom.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.fmm.testebottom.R;
import com.fmm.testebottom.models.Pev;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class MapFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private DatabaseReference db;

    private Pev pevs;

    private GoogleMap mMap;
    private FusedLocationProviderClient myLocation;
    private LatLng myPosition;

    private EditText txt_search;
    private ImageButton btn_search;

    private ArrayList<Pev> dataGlobal;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocation = LocationServices.getFusedLocationProviderClient(this.getContext());

        db = FirebaseDatabase.getInstance().getReference("pevs");

        txt_search = view.findViewById(R.id.txt_search);
        btn_search = view.findViewById(R.id.btnSearch);

        dataGlobal = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPev(txt_search.getText().toString());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setPosition() {
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 58));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkCanGetLocation();
        showPoints();
    }


    public void showPoints(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    Iterator<DataSnapshot> data = snapshot.getChildren().iterator();
                    while (data.hasNext()){
                        Pev pev = data.next().getValue(Pev.class);
                        dataGlobal.add(pev);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(
                                        pev.getLocalizacao().getLatitude(),
                                        pev.getLocalizacao().getLongitude()))
                                .title(pev.getNome())
                                .snippet(joinMateriais(pev.getMateriais())))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("WrongConstant")
    private void checkCanGetLocation(){
        if(checkSelfPermission( this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getMyLocationNow();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, getTargetRequestCode());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == getTargetRequestCode()){
          if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
              getMyLocationNow();
          }else {
              Toast.makeText(this.getContext(), "Acesso ao local não permitido!", Toast.LENGTH_SHORT).show();
          }
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocationNow(){
        myLocation.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            setPosition();
                        }
                    }
                });
    }
    
    private String joinMateriais(ArrayList<String> materiais){
        Iterator it = materiais.iterator();
        String material = "";
        while(it.hasNext()){
            material += " " + it.next() + " -";
        }

        return material.substring(0, material.length() -1);
    }

    private boolean searchPev(String nome){
        if(!nome.isEmpty() && dataGlobal != null){
            Iterator<Pev> data = dataGlobal.iterator();
            while(data.hasNext()){
                Pev pev = data.next();
                if(pev.getNome().equalsIgnoreCase(nome)){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            pev.getLocalizacao().getLatitude(),
                            pev.getLocalizacao().getLongitude()), 100));
                    return  true;
                }
            }
        }else {
            return false;
        }
        return false;
    }


}