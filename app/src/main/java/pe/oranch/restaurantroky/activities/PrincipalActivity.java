package pe.oranch.restaurantroky.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

//importar las sesiones
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//fin importado sesiones


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.request.CantCalificacionRequest;
import pe.oranch.restaurantroky.request.CantComentariosRequest;

public class PrincipalActivity extends AppCompatActivity {
    RelativeLayout rl_menuizq, rl_menuder, rl_menuinfizq, rl_menuinfder;
    TextView personasCerca, cantidadComentarios, cantidadCalificaciones;

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //seteando archivo de sesion
        pref = PreferenceManager.getDefaultSharedPreferences(PrincipalActivity.this.getBaseContext());
        //fin de seteado sesion

        //setear datos enviados del logueo
        personasCerca = (TextView) findViewById(R.id.textoPersonasCerca);
        Intent intentdatos = getIntent();
        String nombre = intentdatos.getStringExtra("tay_empresa_nombre");

        personasCerca.setText(pref.getString("tay_empresa_nombre", "").toString());
        //fin seteo de datos

        rl_menuizq = (RelativeLayout) findViewById(R.id.relLayoutMenuIzq);
        rl_menuizq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(PrincipalActivity.this,PersonaActivity.class);
                PrincipalActivity.this.startActivity(intentRegP);
            }
        });

        rl_menuder = (RelativeLayout) findViewById(R.id.relLayoutMenuDer);
        rl_menuder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegMenuDer = new Intent(PrincipalActivity.this,VisitasActivity.class);
                PrincipalActivity.this.startActivity(intentRegMenuDer);
            }
        });

        rl_menuinfizq = (RelativeLayout) findViewById(R.id.relLayoutInferiorMenuIzq);
        rl_menuinfizq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegMenuInfIzq = new Intent(PrincipalActivity.this,ComentariosActivity.class);
                PrincipalActivity.this.startActivity(intentRegMenuInfIzq);
            }
        });

        rl_menuinfder = (RelativeLayout) findViewById(R.id.relLayoutInferiorMenuDer);
        rl_menuinfder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegMenuInfDer = new Intent(PrincipalActivity.this,CalificacionesActivity.class);
                PrincipalActivity.this.startActivity(intentRegMenuInfDer);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:

                        break;
                    case R.id.ic_menu:
                        Intent intentReg = new Intent(PrincipalActivity.this,ComidaPrincipalActivity.class);
                        PrincipalActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg2 = new Intent(PrincipalActivity.this,CartaActivity.class);
                        PrincipalActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg3 = new Intent(PrincipalActivity.this,OfertaActivity.class);
                        PrincipalActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg4 = new Intent(PrincipalActivity.this,PerfilActivity.class);
                        PrincipalActivity.this.startActivity(intentReg4);
                        break;
                }
                return false;
            }
        });

        obtenerCantComentarios( );
        obtenerCantCalificaciones();
    }

    private void obtenerCantComentarios() {
        pref = PreferenceManager.getDefaultSharedPreferences(PrincipalActivity.this.getBaseContext());
        final int idusuario= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

            if(isInternetOn()) {
                Response.Listener<String> responseListenerCantidad = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonReponse = new JSONObject(response);
                            boolean success= jsonReponse.getBoolean("success");

                            if (success){
                                String idempresa = jsonReponse.getString("contador");
                                String textoComentario;
                                    if (idempresa.equals("1")) {
                                        textoComentario = " Comentario";
                                    }
                                    else
                                    {
                                        textoComentario = " Comentarios";
                                    }

                                cantidadComentarios =findViewById(R.id.textoCantidadComentarios);
                                cantidadComentarios.setText(idempresa + textoComentario);
                            }else{
                                AlertDialog.Builder builder= new AlertDialog.Builder(PrincipalActivity.this);
                                builder.setMessage("Error al Contabilizar")
                                        .setNegativeButton("Retry",null)
                                        .create().show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                };


                CantComentariosRequest cantcomentarioRequest = new CantComentariosRequest(idusuario,responseListenerCantidad);
                RequestQueue queue = Volley.newRequestQueue(PrincipalActivity.this);
                queue.add(cantcomentarioRequest);
            }
    }
    private void obtenerCantCalificaciones() {
        pref = PreferenceManager.getDefaultSharedPreferences(PrincipalActivity.this.getBaseContext());
        final int idusuario= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        if(isInternetOn()) {
            Response.Listener<String> responseListenerCantidadCalificaciones = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonReponse = new JSONObject(response);
                        boolean success= jsonReponse.getBoolean("success");

                        if (success){
                            String ncalificacion = jsonReponse.getString("calificacion");
                            String textoCalificaciones;
                            if (ncalificacion.equals("1")) {
                                textoCalificaciones = " Calificación";
                            }
                            else
                            {
                                textoCalificaciones = " Calificaciones";
                            }

                            cantidadCalificaciones =findViewById(R.id.textoCantidadCalificaciones);
                            cantidadCalificaciones.setText(ncalificacion + textoCalificaciones);
                        }else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(PrincipalActivity.this);
                            builder.setMessage("Error al Contabilizar")
                                    .setNegativeButton("Retry",null)
                                    .create().show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            };


            CantCalificacionRequest cantcalificacionRequest = new CantCalificacionRequest(idusuario,responseListenerCantidadCalificaciones);
            RequestQueue queue = Volley.newRequestQueue(PrincipalActivity.this);
            queue.add(cantcalificacionRequest);
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) PrincipalActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


}
