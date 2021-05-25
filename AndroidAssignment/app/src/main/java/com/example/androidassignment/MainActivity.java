package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //declare variable
    public GoogleMap mMap;
    DrawerLayout drawerLayout;
    TextView txtloc;
    Button btntest;
    FusedLocationProviderClient fusedLocationProviderClient;
    String locationstr;
    double Latitude = 0;
    double Longitude = 0;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        txtloc = findViewById(R.id.location);
        btntest = findViewById(R.id.btntest);
        //get location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //share preferences
        sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.getlocation), Toast.LENGTH_SHORT).show();
                getLocation();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng currentloc = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions()
                .position(currentloc)
                .title("Current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentloc));
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            locationstr = address.get(0).getAddressLine(0).charAt(2) + "" + address.get(0).getAddressLine(0).charAt(3);
                            Longitude = address.get(0).getLongitude();
                            Latitude = address.get(0).getLatitude();
                            // set
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("location", locationstr);
                            editor.commit();
                            //txtloc.setText("Address: " + address.get(0));
                            txtloc.setText(getResources().getString(R.string.loc) + locationstr);
                            //google map
                            LatLng currentloc = new LatLng(Latitude, Longitude);
                            mMap.addMarker(new MarkerOptions()
                                    .position(currentloc)
                                    .title("Marker"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentloc));
                            mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtloc.setText("Get location error");
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    public void ClickMenu(View view) {
        //open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //Recreate activity
        recreate();
    }

    public void ClickDashboard(View view) {
        //redirect to forum
        redirectActivity(this, Forum.class);
    }

    public void ClickAboutUs(View view) {
        //redirect to about us
        redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view) {
        //logout
        logout(this);
    }

    public static void logout(Activity activity) {
        //initialize alert
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set title
        builder.setTitle("Logout");
        //set message
        builder.setMessage("Are you sure you want to logout?");
        //yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                MainActivity.redirectActivity(activity, Login.class);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });
        //No button
        //Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    public void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}