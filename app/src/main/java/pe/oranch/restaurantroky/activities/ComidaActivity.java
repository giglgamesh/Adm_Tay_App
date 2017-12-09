package pe.oranch.restaurantroky.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.Tay_entradasAdapter;
import pe.oranch.restaurantroky.entidades.Tay_menu;
import pe.oranch.restaurantroky.request.ObtenerEntradasRequest;
import pe.oranch.restaurantroky.request.RegistrarMenuRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Daniel on 02/11/2017.
 */

public class ComidaActivity extends AppCompatActivity{
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    ScrollView scrollview01;
    //FIN REFRESH LAYOUT

    //variable boton volver y funcionalidades
    RelativeLayout layoutbotonVolver;
    RelativeLayout rellayoutpreciomenos, rellayoutpreciomas;
    Double preciot;
    //fin variable botron volver

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion

    //variables recycler y array
    ArrayList<Tay_menu> listaEntradas;
    ArrayList<Tay_menu> listaEntradasEspeciales;
    ArrayList<String> listaEntradasNuevas;
    ArrayList<Tay_menu> listaSegundos;
    ArrayList<Tay_menu> listaSegundosEspeciales;
    RecyclerView recyclerEntradas;
    RecyclerView recyclerSegundos;
    //fin variables de recycler y array

    //variables del formulario
    EditText textoentradaprincipal, textosegundoprincipal, edittextprecio;
    //fim variables del formulario

    //variable boton subir
    Button btnsubircarta;
    //fin boton subir
    //variable boton agregar nuevas entradas
    Integer habilitador=0;
    ImageView addentradas, addsegundo;
    String cajadetextoentrada=null;
    String cajadetextosegundo=null;
    Integer addmetodo = 0;
    Integer metodo = 0;
    Integer habilitadoredittex=0;
    RecyclerView idrecyclerserviciosnuevos;
    private ArrayAdapter<String> adaptador1;
    //fin variable del boton

    //variable para la fecha
    Date fechayhora=new Date();
    String fecha=null;
    //Variable para los if de fecha (la verdad se deberia usar para todo pero ya pue xD asi nomas )
    Date cDate = new Date();
    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    //fin variable para la fecha actual

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        //INICIALIZAR REFRESH LAYOUT
        scrollview01 = findViewById(R.id.ScrollView01);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        //FIN REFRESH LAYOUT
        //REFRESH
        scrollview01.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollview01.getScrollY();
                if(scrollY == 0) swipeRefreshLayout.setEnabled(true);
                else swipeRefreshLayout.setEnabled(false);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        actualizarItems();

                    }
                },50);
                // cancel the Visual indication of a refresh
                //swipeRefreshLayout.setRefreshing(false);
                //actualizarItems();
            }
        });
        //FIN REFRESH

        //enlazando el boton a la variable
        btnsubircarta = (Button) findViewById(R.id.Btn_subir_carta);
        //fin enlase

        //enlazando al ImageView para subir nuevas entradas
        addentradas = (ImageView) findViewById(R.id.addEntradas);
        addsegundo = (ImageView) findViewById(R.id.addSegundo);
        //fin enlase

        //boton volver funcionalidad
        layoutbotonVolver = (RelativeLayout) findViewById(R.id.botonVolver);
        layoutbotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(ComidaActivity.this,ComidaPrincipalActivity.class);
                ComidaActivity.this.startActivity(intentRegP);
            }
        });
        //fin boton volver

        //FUNCIONALIDAD +  y -
        edittextprecio = (EditText) findViewById(R.id.editTextPrecio);
        rellayoutpreciomenos = (RelativeLayout) findViewById(R.id.relLayoutPrecioMenos);
        rellayoutpreciomas = (RelativeLayout) findViewById(R.id.relLayoutPrecioMas);
        rellayoutpreciomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preciot=0.0;
                if ((edittextprecio.getText().toString()).equals("")){
                    edittextprecio.setText(Double.toString(preciot+1));
                }else{
                    preciot = Double.parseDouble(edittextprecio.getText().toString());
                    edittextprecio.setText(Double.toString(preciot+1));
                }
            }
        });
        rellayoutpreciomenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preciot=0.0;
                if ((edittextprecio.getText().toString()).equals("") ||(edittextprecio.getText().toString()).equals("0")||(edittextprecio.getText().toString()).equals("0.0")||(edittextprecio.getText().toString()).equals("0.00") ){
                    edittextprecio.setText(Double.toString(preciot));
                }else{
                    preciot = Double.parseDouble(edittextprecio.getText().toString());
                    edittextprecio.setText(Double.toString(preciot-1.0));
                }
            }
        });
        //FIN FUNCIONALIDAD

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intentReg = new Intent(ComidaActivity.this,PrincipalActivity.class);
                        ComidaActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:

                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(ComidaActivity.this,CartaActivity.class);
                        ComidaActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg4 = new Intent(ComidaActivity.this,OfertaActivity.class);
                        ComidaActivity.this.startActivity(intentReg4);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg5 = new Intent(ComidaActivity.this,PerfilActivity.class);
                        ComidaActivity.this.startActivity(intentReg5);
                        break;

                }
                return false;
            }
        });
        obtenerEntradas();
    }
    //FUNCION PARA ACTUALIZAR EL SWIPEREFRESH
    public void actualizarItems(){
        if (habilitador==0){
            pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
            final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

            Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        listaEntradas = new ArrayList<>();
                        listaEntradasEspeciales = new ArrayList<>();
                        listaSegundos = new ArrayList<>();
                        listaSegundosEspeciales = new ArrayList<>();
                        edittextprecio = findViewById(R.id.editTextPrecio);
                        //variables para las entradas
                        textoentradaprincipal = findViewById(R.id.textoEntradaPrincipal);
                        recyclerEntradas = findViewById(R.id.idRecyclerServicios);
                        recyclerEntradas.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                        recyclerEntradas.setHasFixedSize(true);
                        //fin variables entradas

                        //variables para los segundos
                        textosegundoprincipal = findViewById(R.id.textoSegundoPrincipal);
                        recyclerSegundos = findViewById(R.id.idRecyclerServicios2);
                        recyclerSegundos.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                        recyclerSegundos.setHasFixedSize(true);
                        //fin variables segundos


                        //variable para la fecha
                        fecha=(fechayhora.getYear()+1900)+"/"+(fechayhora.getMonth()+1)+"/"+fechayhora.getDate();
                        //fin variable de la fecha

                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_menu tay_menu=null;
                        Tay_menu tay_menusegundo=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");

                        for (int i=0;i<json.length();i++){
                            tay_menu=new Tay_menu();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);
                            //Experimento inicio (Obtiene el registro de la BD y los selecciona segun el tipo)
                            if(jsonObject.optString("tay_menu_tipo").equals("1")){
                                if(jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaEntradas.add(tay_menu);
                                }
                            }
                            if(jsonObject.optString("tay_menu_tipo").equals("2")){
                                if (jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaSegundos.add(tay_menu);
                                }
                            }
                            //fin experimento
                        }
                        //experimento para la lista especial (TRANSFORMA LOS ARRAYLIST QUE ESTAN SEPARADOS POR TIPO A UN ARRAYLIST ESPECIAL PARA LLENAR LA LISTA Y EL PRIMER CAMPO POR SEPARADO)
                        final String jsonentradas = new Gson().toJson(listaEntradas);
                        Type foundListType = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                        JSONArray jsonarrayentradas = new JSONArray(jsonentradas);
                        //validar si hay entradas
                        if (listaEntradas.size()==0){
                            //habilitador =1;
                            //Toast.makeText(ComidaActivity.this,"NO HAY ENTRADAS",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //habilitadoredittex=1;
                            //habilitador = 0;
                            JSONObject jsonObjectTextoEntrada=null;
                            jsonObjectTextoEntrada=jsonarrayentradas.getJSONObject(jsonarrayentradas.length()-1);
                            edittextprecio.setText(jsonObjectTextoEntrada.optString("tay_menu_precio"));
                            textoentradaprincipal.setText(jsonObjectTextoEntrada.optString("tay_menu_nombre"));
                            for (int i=0;i<jsonarrayentradas.length()-1;i++){
                                tay_menu=new Tay_menu();
                                JSONObject jsonObjectEntradas=null;
                                jsonObjectEntradas=jsonarrayentradas.getJSONObject(i);
                                tay_menu.setTay_menu_id(jsonObjectEntradas.optInt("tay_menu_id"));
                                tay_menu.setTay_menu_nombre(jsonObjectEntradas.optString("tay_menu_nombre"));
                                listaEntradasEspeciales.add(tay_menu);
                            }
                            //ta por las webas pero vale la pena probar
                            //List<Tay_menu> jsonentradas2 = new Gson().fromJson(jsonentradas, foundListType);
                            //fin por las webas
                        }
                        //fin validacion de entradas
                        final String jsonsegundos = new Gson().toJson(listaSegundos);
                        Type foundListTypeSegundos = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                        JSONArray jsonarraysegundos = new JSONArray(jsonsegundos);
                        //validar si hay registros
                        if (listaSegundos.size()==0){
                            //habilitador =1;
                            //Toast.makeText(ComidaActivity.this,"NO HAY SEGUNDOS",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //habilitador =0;
                            //habilitadoredittex=1;
                            JSONObject jsonObjectTextoSegundo=null;
                            jsonObjectTextoSegundo=jsonarraysegundos.getJSONObject(jsonarraysegundos.length()-1);
                            textosegundoprincipal.setText(jsonObjectTextoSegundo.optString("tay_menu_nombre"));
                            for (int i=0;i<jsonarraysegundos.length()-1;i++){
                                tay_menusegundo=new Tay_menu();
                                JSONObject jsonObjectSegundos=null;
                                jsonObjectSegundos=jsonarraysegundos.getJSONObject(i);
                                tay_menusegundo.setTay_menu_id(jsonObjectSegundos.optInt("tay_menu_id"));
                                tay_menusegundo.setTay_menu_nombre(jsonObjectSegundos.optString("tay_menu_nombre"));
                                listaSegundosEspeciales.add(tay_menusegundo);
                            }
                            //ta por las webas pero vale la pena probar
                            //List<Tay_menu> jsonsegundos2 = new Gson().fromJson(jsonsegundos, foundListTypeSegundos);
                            //fin por las webas
                        }
                        //fin validacion de registros
                        //fin experimento
                        Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaEntradasEspeciales);
                        recyclerEntradas.setAdapter(adapter);
                        Tay_entradasAdapter adapter2=new Tay_entradasAdapter(ComidaActivity.this,listaSegundosEspeciales);
                        recyclerSegundos.setAdapter(adapter2);
                    }catch (JSONException e){
                        e.printStackTrace();
                        habilitador=1;
                        //cambiaste de posicion los botones de agregado acuerdate
                    }
                }
            };
            ObtenerEntradasRequest obtenerentradasRequest = new ObtenerEntradasRequest(empresa,responseListenerLista);
            RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
            queue.add(obtenerentradasRequest);
            //habilitador=0;
            habilitadoredittex=1;
        }else if (habilitador==1){
            pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
            final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

            Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        listaEntradas = new ArrayList<>();
                        listaEntradasEspeciales = new ArrayList<>();
                        listaSegundos = new ArrayList<>();
                        listaSegundosEspeciales = new ArrayList<>();
                        edittextprecio = findViewById(R.id.editTextPrecio);
                        //variables para las entradas
                        textoentradaprincipal = findViewById(R.id.textoEntradaPrincipal);
                        recyclerEntradas = findViewById(R.id.idRecyclerServicios);
                        recyclerEntradas.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                        recyclerEntradas.setHasFixedSize(true);
                        //fin variables entradas

                        //variables para los segundos
                        textosegundoprincipal = findViewById(R.id.textoSegundoPrincipal);
                        recyclerSegundos = findViewById(R.id.idRecyclerServicios2);
                        recyclerSegundos.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                        recyclerSegundos.setHasFixedSize(true);
                        //fin variables segundos


                        //variable para la fecha
                        fecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                        //fin variable de la fecha

                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_menu tay_menu=null;
                        Tay_menu tay_menusegundo=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");

                        for (int i=0;i<json.length();i++){
                            tay_menu=new Tay_menu();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);
                            //Experimento inicio (Obtiene el registro de la BD y los selecciona segun el tipo)
                            if(jsonObject.optString("tay_menu_tipo").equals("1")){
                                if(jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaEntradas.add(tay_menu);
                                }
                            }
                            if(jsonObject.optString("tay_menu_tipo").equals("2")){
                                if (jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaSegundos.add(tay_menu);
                                }
                            }
                            //fin experimento
                        }
                        //experimento para la lista especial (TRANSFORMA LOS ARRAYLIST QUE ESTAN SEPARADOS POR TIPO A UN ARRAYLIST ESPECIAL PARA LLENAR LA LISTA Y EL PRIMER CAMPO POR SEPARADO)
                        final String jsonentradas = new Gson().toJson(listaEntradas);
                        Type foundListType = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                        JSONArray jsonarrayentradas = new JSONArray(jsonentradas);
                        //validar si hay entradas
                        if (listaEntradas.size()==0){
                            habilitador =1;
                            //Toast.makeText(ComidaActivity.this,"NO HAY ENTRADAS",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            habilitadoredittex=1;
                            habilitador = 0;
                            JSONObject jsonObjectTextoEntrada=null;
                            jsonObjectTextoEntrada=jsonarrayentradas.getJSONObject(jsonarrayentradas.length()-1);
                            edittextprecio.setText(jsonObjectTextoEntrada.optString("tay_menu_precio"));
                            textoentradaprincipal.setText(jsonObjectTextoEntrada.optString("tay_menu_nombre"));
                            for (int i=0;i<jsonarrayentradas.length()-1;i++){
                                tay_menu=new Tay_menu();
                                JSONObject jsonObjectEntradas=null;
                                jsonObjectEntradas=jsonarrayentradas.getJSONObject(i);
                                tay_menu.setTay_menu_id(jsonObjectEntradas.optInt("tay_menu_id"));
                                tay_menu.setTay_menu_nombre(jsonObjectEntradas.optString("tay_menu_nombre"));
                                listaEntradasEspeciales.add(tay_menu);
                            }
                            //ta por las webas pero vale la pena probar
                            //List<Tay_menu> jsonentradas2 = new Gson().fromJson(jsonentradas, foundListType);
                            //fin por las webas
                        }
                        //fin validacion de entradas
                        final String jsonsegundos = new Gson().toJson(listaSegundos);
                        Type foundListTypeSegundos = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                        JSONArray jsonarraysegundos = new JSONArray(jsonsegundos);
                        //validar si hay registros
                        if (listaSegundos.size()==0){
                            habilitador =1;
                            //Toast.makeText(ComidaActivity.this,"NO HAY SEGUNDOS",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            habilitador =0;
                            habilitadoredittex=1;
                            JSONObject jsonObjectTextoSegundo=null;
                            jsonObjectTextoSegundo=jsonarraysegundos.getJSONObject(jsonarraysegundos.length()-1);
                            textosegundoprincipal.setText(jsonObjectTextoSegundo.optString("tay_menu_nombre"));
                            for (int i=0;i<jsonarraysegundos.length()-1;i++){
                                tay_menusegundo=new Tay_menu();
                                JSONObject jsonObjectSegundos=null;
                                jsonObjectSegundos=jsonarraysegundos.getJSONObject(i);
                                tay_menusegundo.setTay_menu_id(jsonObjectSegundos.optInt("tay_menu_id"));
                                tay_menusegundo.setTay_menu_nombre(jsonObjectSegundos.optString("tay_menu_nombre"));
                                listaSegundosEspeciales.add(tay_menusegundo);
                            }
                            //ta por las webas pero vale la pena probar
                            //List<Tay_menu> jsonsegundos2 = new Gson().fromJson(jsonsegundos, foundListTypeSegundos);
                            //fin por las webas
                        }
                        //fin validacion de registros
                        //fin experimento
                        Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaEntradasEspeciales);
                        recyclerEntradas.setAdapter(adapter);
                        Tay_entradasAdapter adapter2=new Tay_entradasAdapter(ComidaActivity.this,listaSegundosEspeciales);
                        recyclerSegundos.setAdapter(adapter2);
                    }catch (JSONException e){
                        e.printStackTrace();
                        habilitador=1;
                        //cambiaste de posicion los botones de agregado acuerdate
                    }
                }
            };
            ObtenerEntradasRequest obtenerentradasRequest = new ObtenerEntradasRequest(empresa,responseListenerLista);
            RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
            queue.add(obtenerentradasRequest);
            //habilitador=0;
        }
    }
    //FIN DE LA FUNCION

    public void obtenerEntradas() {
        pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaEntradas = new ArrayList<>();
                    listaEntradasEspeciales = new ArrayList<>();
                    listaSegundos = new ArrayList<>();
                    listaSegundosEspeciales = new ArrayList<>();
                    edittextprecio = findViewById(R.id.editTextPrecio);
                    //variables para las entradas
                    textoentradaprincipal = findViewById(R.id.textoEntradaPrincipal);
                    recyclerEntradas = findViewById(R.id.idRecyclerServicios);
                    recyclerEntradas.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                    recyclerEntradas.setHasFixedSize(true);
                    //fin variables entradas

                    //variables para los segundos
                    textosegundoprincipal = findViewById(R.id.textoSegundoPrincipal);
                    recyclerSegundos = findViewById(R.id.idRecyclerServicios2);
                    recyclerSegundos.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                    recyclerSegundos.setHasFixedSize(true);
                    //fin variables segundos


                    //variable para la fecha
                    fecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                    //fin variable de la fecha
                    //Inicio funcionalidad del boton aprobar
                    btnsubircarta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (habilitador==0){
                                AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                builder.setMessage("NO HAY NUEVOS DATOS PARA REGISTRAR")
                                        .setNegativeButton("Ok",null)
                                        .create().show();
                            }else {
                                if(metodo==1){
                                    if (inputValidationEntrada()){
                                        if (habilitadoredittex==2 ){
                                            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                            builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }else{
                                            //Toast.makeText(ComidaActivity.this, "COMPROBAR Y REGISTRAR ENTRADA:" + (listaEntradas.size() + listaSegundos.size()), Toast.LENGTH_SHORT).show();
                                            //VARIABLES PARA EL REGISTRO
                                            pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
                                            final int menuempresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                            int menutipo = 0;
                                            String menunombre=null;
                                            Double menuprecio = 0.00;
                                            String menufecha = null;
                                            //
                                            //REGISTRAR LA ENTRADA
                                            menutipo =1;
                                            menunombre = textoentradaprincipal.getText().toString();
                                            menuprecio = Double.parseDouble(edittextprecio.getText().toString());
                                            menufecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                                            Response.Listener<String> responseListenerRegistrarMenu = new Response.Listener<String>(){
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
                                            RegistrarMenuRequest registrarmenuRequest = new RegistrarMenuRequest(menutipo,menunombre,menuprecio,menufecha,menuempresa,responseListenerRegistrarMenu);
                                            RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
                                            queue.add(registrarmenuRequest);
                                            //FIN REGISTRO DE LA ENTRADA
                                            habilitadoredittex=2;
                                            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                            builder.setMessage("REGISTRO DE ENTRADA CON EXITO")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }
                                    }
                                }else if (metodo == 2){
                                    if (inputValidationSegundo()){
                                        if (habilitadoredittex==2){
                                            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                            builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }else{
                                            //Toast.makeText(ComidaActivity.this, "COMPROBAR Y REGISTRAR SEGUNDO:" + (listaEntradas.size() + listaSegundos.size()), Toast.LENGTH_SHORT).show();
                                            //VARIABLES PARA EL REGISTRO
                                            pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
                                            final int menuempresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                            int menutipo = 0;
                                            String menunombre=null;
                                            Double menuprecio = 0.00;
                                            String menufecha = null;
                                            //
                                            //REGISTRAR LA ENTRADA
                                            menutipo =2;
                                            menunombre = textosegundoprincipal.getText().toString();
                                            menuprecio = Double.parseDouble(edittextprecio.getText().toString());
                                            menufecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                                            Response.Listener<String> responseListenerRegistrarMenu = new Response.Listener<String>(){
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
                                            RegistrarMenuRequest registrarmenuRequest = new RegistrarMenuRequest(menutipo,menunombre,menuprecio,menufecha,menuempresa,responseListenerRegistrarMenu);
                                            RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
                                            queue.add(registrarmenuRequest);
                                            //FIN REGISTRO DE LA ENTRADA
                                            habilitadoredittex=2;
                                            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                            builder.setMessage("REGISTRO DE SEGUNDO CON EXITO")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }
                                    }
                                }else if(metodo == 0){
                                    if (inputValidation()) {
                                        //Toast.makeText(ComidaActivity.this, "REGISTRAR :" + (listaEntradas.size() + listaSegundos.size()) + " METODO DE ACTUALIZAR", Toast.LENGTH_SHORT).show();
                                        pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
                                        final int menuempresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                        int menutipo = 0;
                                        String menunombre=null;
                                        Double menuprecio = 0.00;
                                        String menufecha = null;
                                        //AGREGAR MENU ENTRADA Y ALMUERZO
                                        for (int i=1; i<3; i++ ){
                                            switch (i){
                                                case 1:
                                                    //COMIENZO INGRESO ENTRADA
                                                    menutipo =1;
                                                    menunombre = textoentradaprincipal.getText().toString();
                                                    menuprecio = Double.parseDouble(edittextprecio.getText().toString());
                                                    menufecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                                                    Response.Listener<String> responseListenerRegistrarMenu = new Response.Listener<String>(){
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
                                                    RegistrarMenuRequest registrarmenuRequest = new RegistrarMenuRequest(menutipo,menunombre,menuprecio,menufecha,menuempresa,responseListenerRegistrarMenu);
                                                    RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
                                                    queue.add(registrarmenuRequest);
                                                    //FIN INGRESO ENTRADA
                                                    break;
                                                case 2:
                                                    //COMIENZO INGRESO SEGUNDO
                                                    menutipo =2;
                                                    menunombre = textosegundoprincipal.getText().toString();
                                                    menuprecio = Double.parseDouble(edittextprecio.getText().toString());
                                                    menufecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                                                    Response.Listener<String> responseListenerc2RegistrarMenu = new Response.Listener<String>(){
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
                                                    RegistrarMenuRequest registrarmenuc2Request = new RegistrarMenuRequest(menutipo,menunombre,menuprecio,menufecha,menuempresa,responseListenerc2RegistrarMenu);
                                                    RequestQueue queuer = Volley.newRequestQueue(ComidaActivity.this);
                                                    queuer.add(registrarmenuc2Request);
                                                    //FIN INGRESO SEGUNDO
                                                    break;
                                            }
                                        }
                                        habilitador=0;
                                        habilitadoredittex=2;
                                        AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                        builder.setMessage("REGISTRO CON EXITO")
                                                .setNegativeButton("Ok",null)
                                                .create().show();
                                        //FIN ENTRADA Y ALMUERZO
                                    }
                                }
                            }

                        }
                    });
                    //fin funcionalidad del boton aprobar
                    //funcionalidad boton agregar nuevas comidas
                    addentradas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cajadetextoentrada = textoentradaprincipal.getText().toString();
                            Tay_menu tay_menunuevo=null;

                                if (cajadetextoentrada.matches("") & habilitador==0)
                                {
                                    Toast.makeText(ComidaActivity.this, "CAMPO PRINCIPAL VACIO", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if (habilitadoredittex==0){
                                        AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                        builder.setMessage("SUBIDA DE PLATO EN PROCESO")
                                                .setNegativeButton("Ok",null)
                                                .create().show();
                                    }else if(habilitadoredittex==1){
                                        addmetodo=1;
                                        //Toast.makeText(ComidaActivity.this,"METODO DE AGREGADO"+addmetodo,Toast.LENGTH_SHORT).show();
                                        //PONER EL CAMPO VACIO Y ACTUALIZA
                                        //Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaEntradas);
                                        //recyclerEntradas.setAdapter(adapter);
                                        entradas();
                                        textoentradaprincipal.setText("");
                                        //FIN ACTUALIZAR Y PONER EL CAMPO VACIO
                                        habilitadoredittex=0;
                                        habilitador=1;
                                        metodo=1;
                                    } else if (habilitadoredittex==2){
                                        AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                        builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                                .setNegativeButton("Ok",null)
                                                .create().show();
                                    }
                                }
                            }
                    });
                    //fin funcionalidad del boton de agregado
                    //funcionalidad del boton de agregado de segundos
                    addsegundo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cajadetextosegundo = textosegundoprincipal.getText().toString();
                            Tay_menu tay_menunuevo=null;
                            if (cajadetextosegundo.matches("") & habilitador==0)
                            {
                                Toast.makeText(ComidaActivity.this, "CAMPO PRINCIPAL VACIO", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if (habilitadoredittex==0){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                    builder.setMessage("SUBIDA DE PLATO EN PROCESO")
                                            .setNegativeButton("Ok",null)
                                            .create().show();
                                }else if(habilitadoredittex==1){
                                    addmetodo=1;
                                    //Toast.makeText(ComidaActivity.this,"METODO DE AGREGADO"+addmetodo,Toast.LENGTH_SHORT).show();
                                    //PONER EL CAMPO VACIO Y ACTUALIZA
                                    //Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaSegundos);
                                    //recyclerSegundos.setAdapter(adapter);
                                    segundos();
                                    textosegundoprincipal.setText("");
                                    //FIN ACTUALIZAR Y PONER EL CAMPO VACIO
                                    habilitadoredittex=0;
                                    habilitador=1;
                                    metodo=2;
                                } else if (habilitadoredittex==2){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
                                    builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                            .setNegativeButton("Ok",null)
                                            .create().show();
                                }
                            }
                        }
                    });
                    //fin funcionalidad boton agregado segundos
                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_menu tay_menu=null;
                    Tay_menu tay_menusegundo=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");

                    for (int i=0;i<json.length();i++){
                        tay_menu=new Tay_menu();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);
                        //Experimento inicio (Obtiene el registro de la BD y los selecciona segun el tipo)
                            if(jsonObject.optString("tay_menu_tipo").equals("1")){
                                if(jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaEntradas.add(tay_menu);
                                }
                            }
                            if(jsonObject.optString("tay_menu_tipo").equals("2")){
                                if (jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                    tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                    tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                    tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                    tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                    tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                    listaSegundos.add(tay_menu);
                                }
                            }
                        //fin experimento
                    }
                    //experimento para la lista especial (TRANSFORMA LOS ARRAYLIST QUE ESTAN SEPARADOS POR TIPO A UN ARRAYLIST ESPECIAL PARA LLENAR LA LISTA Y EL PRIMER CAMPO POR SEPARADO)
                    final String jsonentradas = new Gson().toJson(listaEntradas);
                    Type foundListType = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                    JSONArray jsonarrayentradas = new JSONArray(jsonentradas);
                    //validar si hay entradas
                    if (listaEntradas.size()==0){
                        //Toast.makeText(ComidaActivity.this,"NO HAY ENTRADAS",Toast.LENGTH_SHORT).show();
                        habilitador=1;
                    }
                    else{
                        habilitador=0;
                        habilitadoredittex=1;
                        JSONObject jsonObjectTextoEntrada=null;
                        jsonObjectTextoEntrada=jsonarrayentradas.getJSONObject(jsonarrayentradas.length()-1);
                        edittextprecio.setText(jsonObjectTextoEntrada.optString("tay_menu_precio"));
                        textoentradaprincipal.setText(jsonObjectTextoEntrada.optString("tay_menu_nombre"));
                        for (int i=0;i<jsonarrayentradas.length()-1;i++){
                            tay_menu=new Tay_menu();
                            JSONObject jsonObjectEntradas=null;
                            jsonObjectEntradas=jsonarrayentradas.getJSONObject(i);
                            tay_menu.setTay_menu_id(jsonObjectEntradas.optInt("tay_menu_id"));
                            tay_menu.setTay_menu_nombre(jsonObjectEntradas.optString("tay_menu_nombre"));
                            listaEntradasEspeciales.add(tay_menu);
                        }
                        //ta por las webas pero vale la pena probar
                        //List<Tay_menu> jsonentradas2 = new Gson().fromJson(jsonentradas, foundListType);
                        //fin por las webas
                    }
                    //fin validacion de entradas
                    final String jsonsegundos = new Gson().toJson(listaSegundos);
                    Type foundListTypeSegundos = new TypeToken< ArrayList<Tay_menu>>(){}.getType();
                    JSONArray jsonarraysegundos = new JSONArray(jsonsegundos);
                    //validar si hay registros
                    if (listaSegundos.size()==0){
                        //Toast.makeText(ComidaActivity.this,"NO HAY SEGUNDOS",Toast.LENGTH_SHORT).show();
                        habilitador=1;
                    }
                    else{
                        habilitador=0;
                        habilitadoredittex=1;
                        metodo=2;
                        JSONObject jsonObjectTextoSegundo=null;
                        jsonObjectTextoSegundo=jsonarraysegundos.getJSONObject(jsonarraysegundos.length()-1);
                        textosegundoprincipal.setText(jsonObjectTextoSegundo.optString("tay_menu_nombre"));
                        for (int i=0;i<jsonarraysegundos.length()-1;i++){
                            tay_menusegundo=new Tay_menu();
                            JSONObject jsonObjectSegundos=null;
                            jsonObjectSegundos=jsonarraysegundos.getJSONObject(i);
                            tay_menusegundo.setTay_menu_id(jsonObjectSegundos.optInt("tay_menu_id"));
                            tay_menusegundo.setTay_menu_nombre(jsonObjectSegundos.optString("tay_menu_nombre"));
                            listaSegundosEspeciales.add(tay_menusegundo);
                        }
                        //ta por las webas pero vale la pena probar
                        //List<Tay_menu> jsonsegundos2 = new Gson().fromJson(jsonsegundos, foundListTypeSegundos);
                        //fin por las webas
                    }
                    //fin validacion de registros
                    //fin experimento
                    Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaEntradasEspeciales);
                    recyclerEntradas.setAdapter(adapter);
                    Tay_entradasAdapter adapter2=new Tay_entradasAdapter(ComidaActivity.this,listaSegundosEspeciales);
                    recyclerSegundos.setAdapter(adapter2);

                }catch (JSONException e){
                    e.printStackTrace();
                    habilitador=1;
                    //cambiaste de posicion los botones de agregado acuerdate
                }
            }
        };
        ObtenerEntradasRequest obtenerentradasRequest = new ObtenerEntradasRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
        queue.add(obtenerentradasRequest);
    }
    public void entradas(){
        //OBTENER NUEVOS DATOS
        pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{
                    listaEntradas = new ArrayList<>();
                    //variables para las entradas
                    textoentradaprincipal = findViewById(R.id.textoEntradaPrincipal);
                    recyclerEntradas = findViewById(R.id.idRecyclerServicios);
                    recyclerEntradas.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                    recyclerEntradas.setHasFixedSize(true);
                    //fin variables entradas

                    //variable para la fecha
                    fecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                    //fin variable de la fecha
                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_menu tay_menu=null;
                    Tay_menu tay_menusegundo=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");

                    for (int i=0;i<json.length();i++){
                        tay_menu=new Tay_menu();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);
                        //Experimento inicio (Obtiene el registro de la BD y los selecciona segun el tipo)
                        if(jsonObject.optString("tay_menu_tipo").equals("1")){
                            if(jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                listaEntradas.add(tay_menu);
                            }
                        }
                    }
                    Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaEntradas);
                    recyclerEntradas.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                    habilitador=1;
                    //cambiaste de posicion los botones de agregado acuerdate
                }
            }
        };
        ObtenerEntradasRequest obtenerentradasRequest = new ObtenerEntradasRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
        queue.add(obtenerentradasRequest);
        //FINALIZAR OBTENER DATOS
    }
    public void segundos(){
        //OBTENER NUEVOS DATOS
        pref = PreferenceManager.getDefaultSharedPreferences(ComidaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{
                    listaSegundos = new ArrayList<>();
                    //variables para las entradas
                    textosegundoprincipal = findViewById(R.id.textoSegundoPrincipal);
                    recyclerSegundos = findViewById(R.id.idRecyclerServicios2);
                    recyclerSegundos.setLayoutManager(new LinearLayoutManager(ComidaActivity.this));
                    recyclerSegundos.setHasFixedSize(true);
                    //fin variables entradas

                    //variable para la fecha
                    fecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate();
                    //fin variable de la fecha
                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_menu tay_menu=null;
                    Tay_menu tay_menusegundo=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");

                    for (int i=0;i<json.length();i++){
                        tay_menu=new Tay_menu();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);
                        //Experimento inicio (Obtiene el registro de la BD y los selecciona segun el tipo)
                        if(jsonObject.optString("tay_menu_tipo").equals("2")){
                            if(jsonObject.optString("tay_menu_fecha").equals(fDate)){
                                tay_menu.setTay_menu_fecha(jsonObject.optString("tay_menu_fecha"));
                                tay_menu.setTay_menu_id(jsonObject.optInt("tay_menu_id"));
                                tay_menu.setTay_menu_tipo(jsonObject.optInt("tay_menu_tipo"));
                                tay_menu.setTay_menu_precio(jsonObject.optDouble("tay_menu_precio"));
                                tay_menu.setTay_menu_nombre(jsonObject.optString("tay_menu_nombre"));
                                listaSegundos.add(tay_menu);
                            }
                        }
                    }
                    Tay_entradasAdapter adapter=new Tay_entradasAdapter(ComidaActivity.this,listaSegundos);
                    recyclerSegundos.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                    habilitador=1;
                    //cambiaste de posicion los botones de agregado acuerdate
                }
            }
        };
        ObtenerEntradasRequest obtenerentradasRequest = new ObtenerEntradasRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(ComidaActivity.this);
        queue.add(obtenerentradasRequest);
        //FINALIZAR OBTENER DATOS
    }
    private boolean inputValidation() {

        if(textoentradaprincipal.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
            builder.setMessage("NO HAS INGRESADO UNA ENTRADA")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }

        if(textosegundoprincipal.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
            builder.setMessage("NO HAS INGRESADO UN SEGUNDO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }

        if(edittextprecio.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
            builder.setMessage("NO HAS INGRESADO EL PRECIO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        return true;
    }
    private boolean inputValidationEntrada() {

        if(textoentradaprincipal.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
            builder.setMessage("NO HAS INGRESADO UNA ENTRADA")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        return true;
    }
    private boolean inputValidationSegundo() {

        if(textosegundoprincipal.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaActivity.this);
            builder.setMessage("NO HAS INGRESADO UN SEGUNDO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        return true;
    }
}

