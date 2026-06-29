package csci3130.fall_2021.group1.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.IOException;
import java.util.List;


public class LocationService extends AppCompatActivity implements OnMapReadyCallback {

    Button button;
    Button near_button;
    private GoogleMap map;
    int nearby = 0;
    boolean permission = false;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_app);
        button = findViewById(R.id.Location_view);
        near_button = findViewById(R.id.nearby);
        client = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        checkmyPermission();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (permission) {
                    Toast toast = Toast.makeText(LocationService.this,
                            "Permissions granted", Toast.LENGTH_SHORT);
                    toast.show();

                    /*supportMapFragment.getMapAsync(appActivity.this::onMapReady);*/
                    getCurrentLocation();




                } else {
                    Toast toast = Toast.makeText(LocationService.this,
                            "Permissions not granted", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }


        });

        near_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (permission) {
                    JobMarkers();


                } else {
                    Toast toast = Toast.makeText(LocationService.this,
                            "Permissions not granted", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }


        });



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();

                for(DataSnapshot child:children){
                    String address = child.child("0").getValue(String.class);
                    if(distance(AddressLocate(address), latLng )){
                        Toast.makeText(LocationService.this, "A job nearby has been posted!", Toast.LENGTH_SHORT).show();
                        JobMarkers();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void JobMarkers(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();


                for(DataSnapshot child:children){
                    String address = child.child("0").getValue(String.class);
                    String name = (String.valueOf(child.getKey()));
                    LatLng location = AddressLocate(address);


                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            MarkerOptions options = new MarkerOptions().position(location).title(name);
                            googleMap.addMarker(options);


                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());


                            MarkerOptions options = new MarkerOptions().position(latLng).title("My Location");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            googleMap.addMarker(options);

                        }
                    });
                }
            }
        });
    }


    public void checkmyPermission() {
        Dexter.withContext(this).withPermission
                (Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                permission = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest,
                                                           PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    public LatLng AddressLocate(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public boolean distance(LatLng Location1, LatLng Location2){
        if(Location1== null || Location2 == null){
            return false;
        }

        double theta = Location1.longitude-Location2.longitude;
        double dist = Math.sin(deg2rad(Location1.latitude)) * Math.sin(deg2rad(Location2.latitude)) + Math.cos(deg2rad(Location1.latitude)) * Math.cos(deg2rad(Location2.latitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        if(dist<=10){
            return true;
        }
        return false;

    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}

