package com.example.mariapiagoicoechea.ofertas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.Calendar;


public class AgregarOferta extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, DatePickerDialog.OnDateSetListener {

    private static final int PICK_MAP_POINT_REQUEST = 999;  // The request code

    private RequestQueue rq;
    private JsonRequest jrq;

    private EditText et_precio_a;
    private EditText et_marca_a;
    private EditText et_categoria_a;
    private EditText et_tipo_a;
    private EditText et_comercio_a;
    private EditText et_fechav_a;
    private Button bt_agregar_a;
    private ImageButton ibt_ubicacion_a;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_oferta);
        ponerLogo();

        inicializarControles();

        configDatePicker();

        rq = Volley.newRequestQueue(this);
        bt_agregar_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOffer();
            }
        });

        ibt_ubicacion_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAMapa();
            }
        });

    }

    private void configDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );



        et_fechav_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void inicializarControles(){
        et_precio_a = findViewById(R.id.listar_txt_precio);
        et_marca_a = findViewById(R.id.txt_marca);
        et_categoria_a = findViewById(R.id.txt_categoria);
        et_tipo_a = findViewById(R.id.txt_mostrar_tipo);
        et_comercio_a = findViewById(R.id.txt_comercio);
        et_fechav_a = findViewById(R.id.txt_fechavenc);
        bt_agregar_a= findViewById(R.id.bt_agregar);
        ibt_ubicacion_a= findViewById(R.id.ibt_ubicacion);
    }

    public void onErrorResponse(VolleyError volleyError) {
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Se registro la oferta" , Toast.LENGTH_SHORT).show();
        limpiarCajas();
    }

    private void limpiarCajas(){
        et_precio_a.setText("");
        et_marca_a.setText("");
        et_categoria_a.setText("");
        et_tipo_a.setText("");
        et_comercio_a.setText("");
        et_fechav_a.setText("");
        latitud=0.0;
        longitud=0.0;
    }

    private void addOffer() {
        String url = "http://192.168.0.200/ofertas/registrar.php?precio=" +  et_precio_a.getText().toString() +
                "&marca=" + upperCaseFirstLetter( et_marca_a.getText().toString().toLowerCase() ) + "&categoria=" + upperCaseFirstLetter ( et_categoria_a.getText().toString().toLowerCase() )+
                "&tipo=" + upperCaseFirstLetter ( et_tipo_a.getText().toString().toLowerCase() )+  "&comercio=" + upperCaseFirstLetter( et_comercio_a.getText().toString().toLowerCase() )+
                "&fecha_venc="+ et_fechav_a.getText().toString()+ "&latitud=" + latitud + "&longitud=" + longitud;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    private static String upperCaseFirstLetter(String str) {
        if (str.isEmpty()) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    private void irAMapa(){
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                latitud = latLng.latitude;
                longitud = latLng.longitude;
            }
        }
    }

    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year+"/"+(month+1)+"/"+dayOfMonth;
        et_fechav_a.setText(date);
    }
}