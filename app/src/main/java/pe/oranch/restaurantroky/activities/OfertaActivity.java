package pe.oranch.restaurantroky.activities;

import android.app.DatePickerDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.Comentario_innerjoin_clienteAdapter;
import pe.oranch.restaurantroky.entidades.Comentario_innerjoin_cliente;
import pe.oranch.restaurantroky.recursos.LinedEditText;
import pe.oranch.restaurantroky.request.CalificacionPorPersonaRequest;
import pe.oranch.restaurantroky.request.CantCalificacionRequest;
import pe.oranch.restaurantroky.request.DescuentoActualizarRequest;
import pe.oranch.restaurantroky.request.ListarComentariosRequest;
import pe.oranch.restaurantroky.request.ListarDescuentoRequest;
import pe.oranch.restaurantroky.request.PromedioCalificacionRequest;
import pe.oranch.restaurantroky.request.UpdateComentariosRequest;

/**
 * Created by Daniel on 02/11/2017.
 */

public class OfertaActivity extends AppCompatActivity{
    ImageView fecha1,fecha2;
    EditText inicio_oferta,fin_oferta;
    private int dia,mes,anio;
    //boton variable
    Button btnsubiroferta;
    StringBuffer sb=null;
    //fin boton variable

    //variables radiobuton
    RadioButton radiobutton5,radiobutton10,radiobutton20,radiobutton25;
    LinedEditText descripcionoferta;
    //fin variables

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intentReg = new Intent(OfertaActivity.this,PrincipalActivity.class);
                        OfertaActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:
                        Intent intentReg2 = new Intent(OfertaActivity.this,ComidaPrincipalActivity.class);
                        OfertaActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(OfertaActivity.this,CartaActivity.class);
                        OfertaActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:

                        break;
                    case R.id.ic_perfil:
                        Intent intentReg4 = new Intent(OfertaActivity.this,PerfilActivity.class);
                        OfertaActivity.this.startActivity(intentReg4);
                        break;

                }
                return false;
            }
        });

        fecha1=(ImageView) findViewById(R.id.imageFecha1);
        fecha2 =(ImageView) findViewById(R.id.imageFecha2);
        inicio_oferta = (EditText) findViewById(R.id.fechaInicioOferta);
        fin_oferta = (EditText) findViewById(R.id.fechaFinOferta);

        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==fecha1){
                    final Calendar c= Calendar.getInstance();
                    dia=c.get(Calendar.DAY_OF_MONTH);
                    mes=c.get(Calendar.MONTH);
                    anio=c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(OfertaActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            inicio_oferta.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                        }
                    }
                    ,anio,mes,dia);
                    datePickerDialog.show();
                }
            }
        });

        fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==fecha2){
                    final Calendar c= Calendar.getInstance();
                    dia=c.get(Calendar.DAY_OF_MONTH);
                    mes=c.get(Calendar.MONTH);
                    anio=c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(OfertaActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            fin_oferta.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                        }
                    }
                            ,anio,mes,dia+1);
                    datePickerDialog.show();
                }
            }
        });
        obtenerDescuento();
    }

    private void obtenerDescuento() {
        pref = PreferenceManager.getDefaultSharedPreferences(OfertaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        if(isInternetOn()) {
            Response.Listener<String> responseListenerDescuento = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonReponse = new JSONObject(response);
                        boolean success= jsonReponse.getBoolean("success");

                        if (success){
                            final Integer descuentoid = jsonReponse.getInt("tay_descuento_id");
                            String descuento = jsonReponse.getString("tay_descuento_descuento");
                            String fechaini = jsonReponse.getString("tay_descuento_fecha");
                            String fechafin = jsonReponse.getString("tay_descuento_fechafin");
                            String descripcion = jsonReponse.getString("tay_descuento_descripcion");
                            if (descuento.equals("5")) {
                                radiobutton5 = (RadioButton) findViewById(R.id.radio_button_5);
                                radiobutton5.setChecked(true);
                            }
                            else
                            {
                                if (descuento.equals("10")) {
                                    radiobutton10 = (RadioButton) findViewById(R.id.radio_button_10);
                                    radiobutton10.setChecked(true);
                                }
                                else
                                {
                                    if (descuento.equals("20")) {
                                        radiobutton20 = (RadioButton) findViewById(R.id.radio_button_20);
                                        radiobutton20.setChecked(true);
                                    }
                                    else
                                    {
                                        if (descuento.equals("25")) {
                                            radiobutton25 = (RadioButton) findViewById(R.id.radio_button_25);
                                            radiobutton25.setChecked(true);
                                        }
                                        else
                                        {

                                        }
                                    }
                                }
                            }

                            descripcionoferta = (LinedEditText) findViewById(R.id.descripcionOferta);
                            descripcionoferta.setText(descripcion);
                            inicio_oferta = (EditText) findViewById(R.id.fechaInicioOferta);
                            fin_oferta = (EditText) findViewById(R.id.fechaFinOferta);
                            inicio_oferta.setText(fechaini);
                            fin_oferta.setText(fechafin);
                            //inicio actividad boton actualizar
                            btnsubiroferta = (Button) findViewById(R.id.Btn_subir_oferta);
                            btnsubiroferta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //declarar variables locales en el clic
                                    inicio_oferta = (EditText) findViewById(R.id.fechaInicioOferta);
                                    fin_oferta = (EditText) findViewById(R.id.fechaFinOferta);
                                    descripcionoferta = (LinedEditText) findViewById(R.id.descripcionOferta);
                                    radiobutton5 = (RadioButton) findViewById(R.id.radio_button_5);
                                    radiobutton10 = (RadioButton) findViewById(R.id.radio_button_10);
                                    radiobutton20 = (RadioButton) findViewById(R.id.radio_button_20);
                                    radiobutton25 = (RadioButton) findViewById(R.id.radio_button_25);
                                    String fechaini=inicio_oferta.getText().toString();
                                    String fechafin=fin_oferta.getText().toString();
                                    String descripcion=descripcionoferta.getText().toString();
                                    Integer descuento = 1;
                                    if (radiobutton5.isChecked()){
                                        descuento=5;

                                    }else
                                    {
                                        if (radiobutton10.isChecked()){
                                            descuento=10;

                                        }else
                                        {
                                            if (radiobutton20.isChecked()){
                                                descuento=20;

                                            }else
                                            {
                                                if (radiobutton25.isChecked()){
                                                    descuento=25;
                                                }else
                                                {
                                                }
                                            }
                                        }
                                    }

                                    //fin declaraciones
                                    //funcion del response
                                    Response.Listener<String> responseListenerUpdateOferta = new Response.Listener<String>(){
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonReponse = new JSONObject(response);
                                            }
                                            catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //fin funcion de l response
                                    DescuentoActualizarRequest descuentoactualizarRequest = new DescuentoActualizarRequest(descuento,fechaini,fechafin,descripcion,descuentoid,responseListenerUpdateOferta);
                                    RequestQueue queue = Volley.newRequestQueue(OfertaActivity.this);
                                    queue.add(descuentoactualizarRequest);
                                    Toast.makeText(OfertaActivity.this,"Ofertas Actualizadas",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OfertaActivity.this,PrincipalActivity.class);
                                    OfertaActivity.this.startActivity(intent);
                                }
                            });
                            //fin boton actualizar
                        }else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(OfertaActivity.this);
                            builder.setMessage("Error al Contabilizar")
                                    .setNegativeButton("Retry",null)
                                    .create().show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            };


            ListarDescuentoRequest listardescuentoRequest = new ListarDescuentoRequest(empresa,responseListenerDescuento);
            RequestQueue queue = Volley.newRequestQueue(OfertaActivity.this);
            queue.add(listardescuentoRequest);
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) OfertaActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
