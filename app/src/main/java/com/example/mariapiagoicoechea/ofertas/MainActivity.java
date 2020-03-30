package com.example.mariapiagoicoechea.ofertas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ACCESS_FINE = 0;

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView txtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ponerLogo();
        inicializarControles();

        //Iniciar sesion FB
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton.setReadPermissions("email");
        loginWithFacebook();



    }


    //Boton loginButton
    public void iniciarSesion(View view){
        Intent iniciarSesion = new Intent(this, Menu.class);
        startActivity(iniciarSesion);
    }

    private void loginWithFacebook(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                txtEstado.setText("Ingreso correcto\n" + loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                txtEstado.setText("Ingreso cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                txtEstado.setText("Error al iniciar sesión " + error.getMessage());
            }
        });
    }

    //Pone el logo en el action bar
    private void ponerLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void inicializarControles (){
        loginButton = (LoginButton) findViewById(R.id.login_b);
        callbackManager = CallbackManager.Factory.create();
    }
}
