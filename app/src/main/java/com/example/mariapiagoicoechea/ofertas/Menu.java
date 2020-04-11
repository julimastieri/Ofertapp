package com.example.mariapiagoicoechea.ofertas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Menu extends AppCompatActivity {

    private Button b_agregar;
    private Button b_buscar;
    private Button b_notificaciones;
    private EditText ipport;
    private String sharedPrefFile = "com.example.android.ipport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ponerLogo();
        inicializarControles();
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        ipport.setText(mPreferences.getString("ipport",""));

    }

    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void inicializarControles(){
        b_agregar = findViewById(R.id.b_menu_agr);
        b_buscar = findViewById(R.id.b_menu_busc);
        b_notificaciones = findViewById(R.id.b_notificaciones);
        ipport = findViewById(R.id.txt_ip_port);
    }

    public void setB_agregar (View view){
        Intent agregar = new Intent(this, AgregarOferta.class);
        agregar.putExtra("ipport", ipport.getText().toString());
        startActivity(agregar);
    }

    public void setB_buscar (View view){
        Intent buscar = new Intent(this, BuscarOferta.class);
        buscar.putExtra("ipport", ipport.getText().toString());
        startActivity(buscar);
    }

    public void setB_Notificaciones (View view){
        Intent notify = new Intent(this, notificationsActivity.class);
        notify.putExtra("ipport", ipport.getText().toString());
        startActivity(notify);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("ipport", ipport.getText().toString());
        preferencesEditor.apply();
    }

}
