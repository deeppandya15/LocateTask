package com.example.locatetask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.locatetask.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient FusedLocationProviderClient;
    BottomNavigationView navigationView;
    Boolean newLocation= false;
    Fragment fragment= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //MAPS......
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //MAPS......

        //navigation object-start

        navigation_botton();
        //navigation object-end


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);

        }
        Task<Location> task = FusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {

                LatLng CurrentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(CurrentLocation)
                        .title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation,10));
            }

        });




    }


    private void navigation_botton() {

        //NAVIGATION
        navigationView = findViewById(R.id.botton_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_add:
                        fragment = new AddFragment();
                        callFragment(fragment);
                        break;
                    case R.id.nav_delete:
                        fragment = new DeleteFragment();
                        callFragment(fragment);
                        break;
                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        callFragment(fragment);
                        break;

                }


                return true;
            }

            private void callFragment(Fragment fragment) {

                /*if(getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    int index = getSupportFragmentManager().getBackStackEntryCount() -1;
                    String tag = getSupportFragmentManager().getBackStackEntryAt(index).getName();
                    Fragment lastFragment = getSupportFragmentManager().findFragmentByTag(tag);
                    getSupportFragmentManager().beginTransaction().remove(lastFragment).commit();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().executePendingTransactions();
                }*/

                   getSupportFragmentManager().beginTransaction().add(R.id.body_container, fragment).addToBackStack(null).commit();

            }


        });

        //NAVIGATION
    }





    @Override
    public void onBackPressed() {

    }

}