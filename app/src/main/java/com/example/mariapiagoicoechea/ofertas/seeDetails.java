package com.example.mariapiagoicoechea.ofertas;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.facebook.internal.AppCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
    
public class seeDetails extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    String marca_bd, categoria_bd, tipo_bd, comercio_bd, fecha_bd;
    Double latitud_bd, longitud_bd, precio_bd;

    private TextView txt_tipo;
    private TextView txt_precio;
    private TextView txt_marca;
    private TextView txt_categoria;
    private TextView txt_fecha;
    private TextView txt_comercio;

    private FusedLocationProviderClient mFusedLocationClient;
    private int locationRequestCode = 1000;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_see_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Ofertapp");

        recibirDatos();

        inicializarControles();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
    }

    private void inicializarControles (){
        txt_tipo = findViewById(R.id.txt_mostrar_tipo);
        txt_precio = findViewById(R.id.txt_mostrar_precio);
        txt_marca = findViewById(R.id.txt_mostrar_marca);
        txt_categoria = findViewById(R.id.txt_mostrar_categoria);
        txt_fecha = findViewById(R.id.txt_mostrar_fecha);
        txt_comercio = findViewById(R.id.txt_mostrar_comercio);

        txt_tipo.setText(tipo_bd);
        txt_precio.setText(precio_bd.toString());
        txt_marca.setText(marca_bd);
        txt_categoria.setText(categoria_bd);
        txt_fecha.setText(fecha_bd);
        txt_comercio.setText(comercio_bd);
    }

    private void recibirDatos (){
        Bundle extras = getIntent().getExtras();
        marca_bd = extras.getString("marca");
        categoria_bd = extras.getString("categoria");
        tipo_bd = extras.getString("tipo");
        comercio_bd = extras.getString("comercio");
        latitud_bd = extras.getDouble("latitud");
        longitud_bd = extras.getDouble("longitud");
        precio_bd = extras.getDouble("precio");
        fecha_bd = extras.getString("fecha");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LatLng comercio= new LatLng(latitud_bd, longitud_bd);
        mMap.addMarker(new MarkerOptions().position(comercio).title(comercio_bd));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(comercio).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mMap.setMyLocationEnabled(true);
                    if (location != null) {
                        LatLng mLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            }
        };

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setMapToolbarEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            // already permission granted
            //getting location
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng mLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                }
            });
        }

        //Accion de pulsacion larga
        mMap.setOnMapLongClickListener(latLng -> {
            mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));

            //Retorno Lat y long
            Intent returnIntent = new Intent();
            returnIntent.putExtra("picked_point", latLng);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        mMap.setMyLocationEnabled(true);
                        if (location != null) {
                            LatLng mLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    });
                } else {
                    Toast.makeText(this, "Permiso de ubicacion denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
