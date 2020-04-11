package com.example.mariapiagoicoechea.ofertas;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

public class notificationsActivity extends AppCompatActivity {

    private final static double DEFAULT_DISTANCE = 1;
    private Button activarNotificaciones, desactivarNotificaciones;
    private EditText et_distancia;

    private String ipport;


    LocationService mLocationService=null;
    boolean mBound=false;

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            mLocationService.removeLocationUpdates();
            if (et_distancia.getText().toString().isEmpty())
                mLocationService.requestLocationUpdates(DEFAULT_DISTANCE, ipport);
            else
                mLocationService.requestLocationUpdates(Double.parseDouble(et_distancia.getText().toString()),ipport);
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {}
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) iBinder;
            mLocationService = binder.getService();
            mBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocationService = null;
            mBound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        et_distancia = findViewById(R.id.edtxt_distancia);

        et_distancia.addTextChangedListener(textWatcher);

        Bundle extras = getIntent().getExtras();
        ipport = extras.getString("ipport");

        Dexter.withActivity(this).withPermissions(Arrays.asList(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ))
                .withListener(new MultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // Checkeo que me haya dado todos los permisos
                        if (report.areAllPermissionsGranted()) {
                            activarNotificaciones = findViewById(R.id.b_activar);
                            desactivarNotificaciones = findViewById(R.id.b_desactivar);

                            activarNotificaciones.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (et_distancia.getText().toString().isEmpty())
                                        mLocationService.requestLocationUpdates(DEFAULT_DISTANCE, ipport);
                                    else {
                                        mLocationService.requestLocationUpdates(Double.parseDouble(et_distancia.getText().toString()), ipport);
                                    }
                                }
                            });
                            desactivarNotificaciones.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLocationService.removeLocationUpdates();
                                }
                            });

                            bindService(new Intent(notificationsActivity.this,
                                            LocationService.class),
                                    mServiceConnection,
                                    Context.BIND_AUTO_CREATE);
                        }

                        // Chekeo si me los denego permanentemente, de ser asi habro app settings
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onStart (){
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(mBound){
            unbindService(mServiceConnection);
            mBound=false;
        }
        super.onStop();
    }
}
