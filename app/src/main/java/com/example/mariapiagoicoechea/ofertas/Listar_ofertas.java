package com.example.mariapiagoicoechea.ofertas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Listar_ofertas extends AppCompatActivity {
    List<Oferta> ofertList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ofertas);
        ponerLogo();

        //get the recycler from XML
        recyclerView = findViewById(R.id.lista_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recibirDatos();

        //Este metodo configura y parsea a json. Despliega en el recyclerview
        loadOferts();
    }

    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void recibirDatos () {
        Intent i = getIntent();
        ofertList = new ArrayList<>();
        ofertList = (List<Oferta>) i.getSerializableExtra("ofertList");
    }


    private void loadOferts() {
        Adapter adapter = new Adapter(Listar_ofertas.this, ofertList);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ver_detalles = new Intent(getApplicationContext(), seeDetails.class);

                String tipo_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getTipo();
                String marca_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getMarca();
                String categoria_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getCategoria();
                String fecha_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getFecha_vencimiento();
                String comercio_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getComercio();
                Double latitud_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getLatitud();
                Double longitud_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getLongitud();
                Double precio_bd = ofertList.get(recyclerView.getChildAdapterPosition(v)).getPrecio();

                ver_detalles.putExtra("tipo", tipo_bd);
                ver_detalles.putExtra("marca", marca_bd);
                ver_detalles.putExtra("categoria", categoria_bd);
                ver_detalles.putExtra("fecha", fecha_bd);
                ver_detalles.putExtra("comercio", comercio_bd);
                ver_detalles.putExtra("latitud", latitud_bd);
                ver_detalles.putExtra("longitud", longitud_bd);
                ver_detalles.putExtra("precio", precio_bd);

                //mando los datos de la oferta en la q se hizo click

                startActivity(ver_detalles);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
