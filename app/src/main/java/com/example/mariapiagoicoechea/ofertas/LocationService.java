package com.example.mariapiagoicoechea.ofertas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationService extends Service {

    private static final String CHANNEL_ID = "my_channel";
    private static final long UPDATE_INTERVAL_IN_MIL=10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MIL=UPDATE_INTERVAL_IN_MIL/2;
    private static final int NOTIFICATION_ID = 1223;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "com.example.mariapiagoicoechea.ofertas"+"started_from:notification";
    private boolean mChangingConfiguration=false;
    private NotificationManager mNotificationManager;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;
    private Location mLocation;

    private List<Oferta> ofertList;
    private double distanciaMax=1;
    private String ipport;

    public  LocationService(){}

    @Override
    public void onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        ofertList = new ArrayList<>();

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread("loc");
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);
        if (startedFromNotification)
        {
            removeLocationUpdates();
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration=true;
    }

    public void removeLocationUpdates() {
        try{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            stopSelf();
        } catch (SecurityException ex) {
            Log.e("Ofertapp", "Permiso de localizacion perdido. "+ex);
        }
    }

    private void getLastLocation() {
        try
        {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                             if (task.isSuccessful() && task.getResult() != null)
                                 mLocation = task.getResult();
                             else
                                 Log.e("Ofertapp", "Fallo en obtener la ubicacion");
                        }

                    });
        }catch (SecurityException ex){
            Log.e("Ofertapp", "Se perdio el permiso de localizacion"+ex);
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MIL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MIL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location lastLocation) {
        mLocation = lastLocation;
        searchInDataBase(mLocation.getLatitude(), mLocation.getLongitude());
    }

    private void searchInDataBase(double latitud, double longitud){

        String URL_ofertas = "http://"+ipport+"/www/buscarPorLocalizacion.php?latitud=" + latitud
                + "&longitud=" + longitud + "&distanciaMax=" + distanciaMax;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_ofertas,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            //convierto el string a json array object
                            JSONArray array = new JSONArray(response);

                            //navegamos a traves de todos los objetos
                            ofertList.clear();
                            for (int i = 0; i < array.length(); i++) {

                                //obtengo una oferta del arreglo json
                                JSONObject oferta = array.getJSONObject(i);

                                //Agregamos la oferta a la lista de ofertas
                                //los nombres igual a la tabla
                                ofertList.add(new Oferta(
                                                oferta.getDouble("precio"),
                                                oferta.getString("marca"),
                                                oferta.getString("categoria"),
                                                oferta.getString("tipo"),
                                                oferta.getString("comercio"),
                                                oferta.getString("fecha_venc"),
                                                oferta.getDouble("latitud"),
                                                oferta.getDouble("longitud")
                                        )
                                );
                            }

                            //Envío notificación
                            mNotificationManager.notify(NOTIFICATION_ID,getNotification());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private Notification getNotification() {

        Intent listarOfertasNotificacion = new Intent(getApplicationContext(), Listar_ofertas.class);
        listarOfertasNotificacion.putExtra("ofertList", (Serializable) ofertList);
        listarOfertasNotificacion.setAction("ofertList");
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this,0,
                listarOfertasNotificacion, PendingIntent.FLAG_UPDATE_CURRENT);


        StringBuilder title = new StringBuilder("Hay "+ ofertList.size());
        if (ofertList.size() == 1)
            title.append(" oferta cerca en un radio de ");
        else
            title.append(" ofertas cerca en un radio de ");


        title.append(distanciaMax+" km");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(activityPendingIntent)
                .setContentText("Toca para ver mas detalles")
                .setContentTitle(title.toString())
                .setColor(ContextCompat.getColor(this, R.color.primaryColor))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Ofertapp")
                .setOngoing(true)
                .setWhen(System.currentTimeMillis());

        //Set channel id for Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId(CHANNEL_ID);
        }
        return builder.build();
    }

    private final IBinder mBinder = new LocalBinder();

    public void requestLocationUpdates(double distanciaMax, String ipport) {
        this.distanciaMax = distanciaMax;
        this.ipport = ipport;

        startService(new Intent(getApplicationContext(),LocationService.class));
        try{
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        } catch (SecurityException ex)
        {
            Log.e("Ofertapp", "Se perdio el permiso de localizacion"+ex);
        }
    }

    public class LocalBinder extends Binder {
        LocationService getService(){return LocationService.this;}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        mChangingConfiguration=false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);
        mChangingConfiguration=false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (!mChangingConfiguration)
                startForeground(NOTIFICATION_ID,getNotification());
        return true;
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacks(null);
        super.onDestroy();
    }
}
