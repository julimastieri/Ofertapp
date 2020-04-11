package com.example.mariapiagoicoechea.ofertas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuscarOferta extends AppCompatActivity {

    private TextInputEditText et_marca_b;
    private TextInputEditText et_categoria_b;
    private TextInputEditText et_tipo_b;
    private TextInputEditText et_comercio_b;
    private List<Oferta> ofertList;
    private String ipport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_oferta);
        ofertList = new ArrayList<>();
        ponerLogo();
        inicializarControles ();
        Bundle extras = getIntent().getExtras();
        ipport = extras.getString("ipport");

    }

    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void inicializarControles (){
        et_marca_b = findViewById(R.id.txt_ip_port);
        et_categoria_b =  findViewById(R.id.txt_categoria_bus);
        et_tipo_b = findViewById(R.id.txt_tipo_bus);
        et_comercio_b = findViewById(R.id.txt_comercio_bus);
    }


    public void setB_buscar (View view){
        searchInDataBase();
    }

    private void searchInDataBase(){

        String URL_ofertas = "http://"+ipport+"/www/buscarmod.php?";

        /*"marca="+et_marca_b.getText().toString()
                +"&categoria="+ et_categoria_b.getText().toString()+"&tipo="+et_tipo_b.getText().toString()+"&comercio="
                + et_comercio_b.getText().toString();*/

        if (!et_marca_b.getText().toString().equals(""))
            URL_ofertas = URL_ofertas + "marca=" + et_marca_b.getText().toString()+ "&";
        if (!et_categoria_b.getText().toString().equals(""))
            URL_ofertas = URL_ofertas + "categoria=" + et_categoria_b.getText().toString()+ "&";
        if (!et_tipo_b.getText().toString().equals(""))
            URL_ofertas = URL_ofertas + "tipo=" + et_tipo_b.getText().toString()+ "&";
        if (!et_comercio_b.getText().toString().equals(""))
            URL_ofertas = URL_ofertas + "comercio=" + et_comercio_b.getText().toString();

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

                            Intent listarOfertas = new Intent(getApplicationContext(), Listar_ofertas.class);
                            listarOfertas.putExtra("ofertList", (Serializable) ofertList);
                            startActivity(listarOfertas);
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

}
