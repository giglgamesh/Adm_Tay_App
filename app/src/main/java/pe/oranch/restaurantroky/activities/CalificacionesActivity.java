package pe.oranch.restaurantroky.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.request.CalificacionPorPersonaRequest;
import pe.oranch.restaurantroky.request.CantCalificacionRequest;
import pe.oranch.restaurantroky.request.PromedioCalificacionRequest;

/**
 * Created by Daniel on 02/11/2017.
 */

public class CalificacionesActivity extends AppCompatActivity{
    RelativeLayout layoutbotonVolver;
    TextView totalPersonas;
    TextView nombreEmpresa;
    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion

    //Variables para las estrellas
    ProgressBar cincoEstrellas, cuatroEstrellas, tresEstrellas, dosEstrellas, unaEstrella;
    //fin variables estrellas

    //Variable texto Rating
    TextView ratinggeneral;
    //Fin variable texto rating

    //RatingBar
    RatingBar estrellasrating;
    //Fin Rating Bar

    //Variable para el texto de personas
    TextView textocinco, textocuatro, textotres, textodos, textouno;
    //fin variable texto de personas

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        //boton volver funcionalidad
        layoutbotonVolver = (RelativeLayout) findViewById(R.id.botonVolver);
        layoutbotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(CalificacionesActivity.this,PrincipalActivity.class);
                CalificacionesActivity.this.startActivity(intentRegP);
            }
        });
        //fin boton volver

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:

                        break;
                    case R.id.ic_menu:
                        Intent intentReg2 = new Intent(CalificacionesActivity.this,ComidaPrincipalActivity.class);
                        CalificacionesActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(CalificacionesActivity.this,CartaActivity.class);
                        CalificacionesActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg4 = new Intent(CalificacionesActivity.this,OfertaActivity.class);
                        CalificacionesActivity.this.startActivity(intentReg4);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg5 = new Intent(CalificacionesActivity.this,PerfilActivity.class);
                        CalificacionesActivity.this.startActivity(intentReg5);
                        break;

                }
                return false;
            }
        });

        obtenerCantidadesPersonas();
    }

    private void obtenerCantidadesPersonas() {
        pref = PreferenceManager.getDefaultSharedPreferences(CalificacionesActivity.this.getBaseContext());
        final int idusuario= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        final String nombreempresa = pref.getString("tay_empresa_nombre", "");
        nombreEmpresa = findViewById(R.id.NombreEmpresa);
        nombreEmpresa.setText(nombreempresa);

        if(isInternetOn()) {
            Response.Listener<String> responseListenerCantidadCalificaciones = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonReponse = new JSONObject(response);
                        boolean success= jsonReponse.getBoolean("success");

                        if (success){
                            String ncalificacion;
                            ncalificacion = jsonReponse.getString("calificacion");
                            totalPersonas =findViewById(R.id.totalPersonasRating);
                            totalPersonas.setText(ncalificacion);
                            //ENCONTRAR ESTRELLAS
                            if (ncalificacion.equals("0")) {
                                ratinggeneral = (TextView) findViewById(R.id.ratingGeneral);
                                ratinggeneral.setText("0");

                                estrellasrating = (RatingBar) findViewById(R.id.estrellasRating);
                                estrellasrating.setRating(0);

                                cincoEstrellas = (ProgressBar) findViewById(R.id.cincoEstrellas);
                                cincoEstrellas.setProgress(0);
                                textocinco = (TextView) findViewById(R.id.textocincoEstrellas);
                                textocinco.setText("0 personas");

                                cuatroEstrellas = (ProgressBar) findViewById(R.id.cuatroEstrellas);
                                cuatroEstrellas.setProgress(0);
                                textocuatro = (TextView) findViewById(R.id.textocuatroEstrellas);
                                textocuatro.setText("0 personas");

                                tresEstrellas = (ProgressBar) findViewById(R.id.tresEstrellas);
                                tresEstrellas.setProgress(0);
                                textotres = (TextView) findViewById(R.id.textotresEstrellas);
                                textotres.setText("0 personas");

                                dosEstrellas = (ProgressBar) findViewById(R.id.dosEstrellas);
                                dosEstrellas.setProgress(0);
                                textodos = (TextView) findViewById(R.id.textodosEstrellas);
                                textodos.setText("0 personas");

                                unaEstrella = (ProgressBar) findViewById(R.id.unaEstrellas);
                                unaEstrella.setProgress(0);
                                textouno = (TextView) findViewById(R.id.textounaEstrellas);
                                textouno.setText("0 personas");
                            }
                            else
                            {
                                //COMIENZO QUERY OBTENER ESTRELLAS PROMEDIO
                                Response.Listener<String> responseListenerPromedioEstrellas = new Response.Listener<String>() {
                                    public void onResponse(String responsePromedio) {
                                        try{
                                            JSONObject jsonReponsePromedio = new JSONObject(responsePromedio);
                                            boolean success= jsonReponsePromedio.getBoolean("success");
                                            if (success){

                                                String nestrellas;
                                                nestrellas = jsonReponsePromedio.getString("promcalificacion");


                                                ratinggeneral = (TextView) findViewById(R.id.ratingGeneral);
                                                ratinggeneral.setText(String.format("%.2f", new BigDecimal(nestrellas)));

                                                estrellasrating = (RatingBar) findViewById(R.id.estrellasRating);
                                                estrellasrating.setRating(Float.parseFloat(nestrellas));
                                            }
                                            else{
                                                AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                builder.setMessage("Error al Contabilizar")
                                                        .setNegativeButton("Retry",null)
                                                        .create().show();
                                            }
                                        }
                                        catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                PromedioCalificacionRequest promedioCalificacionRequest = new PromedioCalificacionRequest(idusuario,responseListenerPromedioEstrellas);
                                RequestQueue queue = Volley.newRequestQueue(CalificacionesActivity.this);
                                queue.add(promedioCalificacionRequest);
                                //FIN ESTRELLAS PROMEDIO
                                //COMIENZO PERSONAS POR ESTRELLAS
                                for (int i=1; i<6; i++ ){
                                    final int  idcalificacion = i;
                                    switch (i){
                                        case 1:
                                            //COMIENZO QUERY OBTENER ESTRELLAS
                                            Response.Listener<String> responseListenerObtenerEstrellas = new Response.Listener<String>() {
                                                public void onResponse(String responseObtenerEstrellas) {
                                                    try{
                                                        JSONObject jsonReponsePromedio = new JSONObject(responseObtenerEstrellas);
                                                        boolean success= jsonReponsePromedio.getBoolean("success");
                                                        if (success){

                                                            String nestrellasporpersona;
                                                            nestrellasporpersona = jsonReponsePromedio.getString("contadorestrellas");

                                                            //seteamos valor de estrella
                                                            unaEstrella = (ProgressBar) findViewById(R.id.unaEstrellas);
                                                            unaEstrella.setProgress(Integer.parseInt(nestrellasporpersona));
                                                            textouno = (TextView) findViewById(R.id.textounaEstrellas);
                                                            textouno.setText(Integer.parseInt(nestrellasporpersona)+" personas");
                                                            //fin del seteo de valores
                                                        }
                                                        else{
                                                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                            builder.setMessage("Error al Contabilizar")
                                                                    .setNegativeButton("Retry",null)
                                                                    .create().show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CalificacionPorPersonaRequest calificacionporPersonaRequest = new CalificacionPorPersonaRequest(idusuario,idcalificacion,responseListenerObtenerEstrellas);
                                            RequestQueue queueporpersona = Volley.newRequestQueue(CalificacionesActivity.this);
                                            queueporpersona.add(calificacionporPersonaRequest);
                                            //FIN OBTENER ESTRELLAS
                                            break;
                                        case 2:
                                            //COMIENZO QUERY OBTENER ESTRELLAS
                                            Response.Listener<String> responseListenerObtenerEstrellas2 = new Response.Listener<String>() {
                                                public void onResponse(String responseObtenerEstrellas) {
                                                    try{
                                                        JSONObject jsonReponsePromedio = new JSONObject(responseObtenerEstrellas);
                                                        boolean success= jsonReponsePromedio.getBoolean("success");
                                                        if (success){

                                                            String nestrellasporpersona;
                                                            nestrellasporpersona = jsonReponsePromedio.getString("contadorestrellas");

                                                            //seteamos valor de estrella
                                                            dosEstrellas = (ProgressBar) findViewById(R.id.dosEstrellas);
                                                            dosEstrellas.setProgress(Integer.parseInt(nestrellasporpersona));
                                                            textodos = (TextView) findViewById(R.id.textodosEstrellas);
                                                            textodos.setText(Integer.parseInt(nestrellasporpersona)+" personas");
                                                            //fin del seteo de valores
                                                        }
                                                        else{
                                                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                            builder.setMessage("Error al Contabilizar")
                                                                    .setNegativeButton("Retry",null)
                                                                    .create().show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CalificacionPorPersonaRequest calificacionporPersonaRequest2 = new CalificacionPorPersonaRequest(idusuario,idcalificacion,responseListenerObtenerEstrellas2);
                                            RequestQueue queueporpersona2 = Volley.newRequestQueue(CalificacionesActivity.this);
                                            queueporpersona2.add(calificacionporPersonaRequest2);
                                            //FIN OBTENER ESTRELLAS
                                            break;
                                        case 3:
                                            //COMIENZO QUERY OBTENER ESTRELLAS
                                            Response.Listener<String> responseListenerObtenerEstrellas3 = new Response.Listener<String>() {
                                                public void onResponse(String responseObtenerEstrellas) {
                                                    try{
                                                        JSONObject jsonReponsePromedio = new JSONObject(responseObtenerEstrellas);
                                                        boolean success= jsonReponsePromedio.getBoolean("success");
                                                        if (success){

                                                            String nestrellasporpersona;
                                                            nestrellasporpersona = jsonReponsePromedio.getString("contadorestrellas");

                                                            //seteamos valor de estrella
                                                            tresEstrellas = (ProgressBar) findViewById(R.id.tresEstrellas);
                                                            tresEstrellas.setProgress(Integer.parseInt(nestrellasporpersona));
                                                            textotres = (TextView) findViewById(R.id.textotresEstrellas);
                                                            textotres.setText(Integer.parseInt(nestrellasporpersona)+" personas");
                                                            //fin del seteo de valores
                                                        }
                                                        else{
                                                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                            builder.setMessage("Error al Contabilizar")
                                                                    .setNegativeButton("Retry",null)
                                                                    .create().show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CalificacionPorPersonaRequest calificacionporPersonaRequest3 = new CalificacionPorPersonaRequest(idusuario,idcalificacion,responseListenerObtenerEstrellas3);
                                            RequestQueue queueporpersona3 = Volley.newRequestQueue(CalificacionesActivity.this);
                                            queueporpersona3.add(calificacionporPersonaRequest3);
                                            //FIN OBTENER ESTRELLAS
                                            break;
                                        case 4:
                                            //COMIENZO QUERY OBTENER ESTRELLAS
                                            Response.Listener<String> responseListenerObtenerEstrellas4 = new Response.Listener<String>() {
                                                public void onResponse(String responseObtenerEstrellas) {
                                                    try{
                                                        JSONObject jsonReponsePromedio = new JSONObject(responseObtenerEstrellas);
                                                        boolean success= jsonReponsePromedio.getBoolean("success");
                                                        if (success){

                                                            String nestrellasporpersona;
                                                            nestrellasporpersona = jsonReponsePromedio.getString("contadorestrellas");

                                                            //seteamos valor de estrella
                                                            cuatroEstrellas = (ProgressBar) findViewById(R.id.cuatroEstrellas);
                                                            cuatroEstrellas.setProgress(Integer.parseInt(nestrellasporpersona));
                                                            textocuatro = (TextView) findViewById(R.id.textocuatroEstrellas);
                                                            textocuatro.setText(Integer.parseInt(nestrellasporpersona)+" personas");
                                                            //fin del seteo de valores
                                                        }
                                                        else{
                                                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                            builder.setMessage("Error al Contabilizar")
                                                                    .setNegativeButton("Retry",null)
                                                                    .create().show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CalificacionPorPersonaRequest calificacionporPersonaRequest4 = new CalificacionPorPersonaRequest(idusuario,idcalificacion,responseListenerObtenerEstrellas4);
                                            RequestQueue queueporpersona4 = Volley.newRequestQueue(CalificacionesActivity.this);
                                            queueporpersona4.add(calificacionporPersonaRequest4);
                                            //FIN OBTENER ESTRELLAS
                                            break;
                                        case 5:
                                            //COMIENZO QUERY OBTENER ESTRELLAS
                                            Response.Listener<String> responseListenerObtenerEstrellas5 = new Response.Listener<String>() {
                                                public void onResponse(String responseObtenerEstrellas) {
                                                    try{
                                                        JSONObject jsonReponsePromedio = new JSONObject(responseObtenerEstrellas);
                                                        boolean success= jsonReponsePromedio.getBoolean("success");
                                                        if (success){

                                                            String nestrellasporpersona;
                                                            nestrellasporpersona = jsonReponsePromedio.getString("contadorestrellas");

                                                            //seteamos valor de estrella
                                                            cincoEstrellas = (ProgressBar) findViewById(R.id.cincoEstrellas);
                                                            cincoEstrellas.setProgress(Integer.parseInt(nestrellasporpersona));
                                                            textocinco = (TextView) findViewById(R.id.textocincoEstrellas);
                                                            textocinco.setText(Integer.parseInt(nestrellasporpersona)+" personas");
                                                            //fin del seteo de valores
                                                        }
                                                        else{
                                                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
                                                            builder.setMessage("Error al Contabilizar")
                                                                    .setNegativeButton("Retry",null)
                                                                    .create().show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CalificacionPorPersonaRequest calificacionporPersonaRequest5 = new CalificacionPorPersonaRequest(idusuario,idcalificacion,responseListenerObtenerEstrellas5);
                                            RequestQueue queueporpersona5 = Volley.newRequestQueue(CalificacionesActivity.this);
                                            queueporpersona5.add(calificacionporPersonaRequest5);
                                            //FIN OBTENER ESTRELLAS
                                            break;
                                    }
                                }
                                //FIN PERSONAS POR ESTRELLAS
                            }
                            //FIN ENCONTRAR ESTRELLAS
                        }else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(CalificacionesActivity.this);
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
            RequestQueue queue = Volley.newRequestQueue(CalificacionesActivity.this);
            queue.add(cantcalificacionRequest);

        }
    }


    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) CalificacionesActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
