package com.example.mariapiagoicoechea.ofertas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    private Button b_agregar;
    private Button b_buscar;
    private Button b_notificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ponerLogo();
        inicializarControles();
    }

    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void inicializarControles(){
        b_agregar = findViewById(R.id.b_menu_agr);
        b_buscar = findViewById(R.id.b_menu_busc);
        b_notificaciones = findViewById(R.id.b_notificaciones);
    }

    public void setB_agregar (View view){
        Intent agregar = new Intent(this, AgregarOferta.class);
        startActivity(agregar);
    }

    public void setB_buscar (View view){
        Intent buscar = new Intent(this, BuscarOferta.class);
        startActivity(buscar);
    }

    public void setB_Notificaciones (View view){
        Intent notify = new Intent(this, notificationsActivity.class);
        startActivity(notify);
    }
}
