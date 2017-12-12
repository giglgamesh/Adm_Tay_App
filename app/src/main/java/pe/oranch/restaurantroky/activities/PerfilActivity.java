package pe.oranch.restaurantroky.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.MyAdapter;
import pe.oranch.restaurantroky.adapter.Tay_comidaListaAdapter;
import pe.oranch.restaurantroky.adapter.Tay_servicioAdapter;
import pe.oranch.restaurantroky.adapter.Tay_servicioListaAdapter;
import pe.oranch.restaurantroky.entidades.Tay_comida;
import pe.oranch.restaurantroky.entidades.Tay_servicio;
import pe.oranch.restaurantroky.entidades.Tay_servicioInner;
import pe.oranch.restaurantroky.request.ListarComidasRequest;
import pe.oranch.restaurantroky.request.ListarServiciosRequest;
import pe.oranch.restaurantroky.request.RegistrarComidaAsocRequest;
import pe.oranch.restaurantroky.request.RegistrarServicioAsocRequest;
import pe.oranch.restaurantroky.request.ServicioRequest;
import pe.oranch.restaurantroky.request.TipoComidaRequest;
import pe.oranch.restaurantroky.request.UpdateComidasRequest;
import pe.oranch.restaurantroky.request.UpdateEmpresaImagenRequest;
import pe.oranch.restaurantroky.request.UpdateEmpresaRequest;
import pe.oranch.restaurantroky.request.UpdateServiciosRequest;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static pe.oranch.restaurantroky.Config.APP_API_URL;
import static pe.oranch.restaurantroky.Config.IMAGENES_URL;

/**
 * Created by Daniel on 02/11/2017.
 */

public class PerfilActivity extends AppCompatActivity{
    RelativeLayout layoutbotonVolver;
    ImageView btnactualizarPerfil;
    ImageView btnactualizarservicios;
    ImageView fotoperfil;
    //VARIABLES PARA LA FOTOGRAFIA
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    //almacenamiento de imagenes
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/perfil/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes/perfil";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;
    //fin almacenamiento de imagenes
    //FIN VARIABLES

    private CircleImageView perfilimagen;
    //Variable para los labels
    TextView nombreEmpresa, direccionEmpresa, telefonoEmpresa, horarioEmpresa, textoComida;
    //fin variable de labels
    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion
    ArrayList<Tay_servicio> listaServicios;
    ArrayList<Tay_comida> listaComidas;
    ArrayList<Tay_servicioInner> listaServiciosEmpresa;
    RecyclerView recyclerServicios;
    RecyclerView idrecycleractualizarservicios;

    //botones layout
    Button btn_guardarup;
    Button btn_guardarupperfil;
    //fin botones layout

    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT

    //botones extras dialog
    EditText horaentrada,minutosentrada,horasalida,minutossalida;
    Button ampmentrada, ampmsalida;
    //fin botones

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(4);
        menuItem.setChecked(true);

        //INICIAR IMAGEVIEW
        perfilimagen = (CircleImageView) findViewById(R.id.perfilImagen);
        fotoperfil = findViewById(R.id.fotoPerfil);
        //FIN INICIALIZAR IMAGEVIEW

        //INICIALIZAR REFRESH LAYOUT
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        //FIN REFRESH LAYOUT
        //REFRESH
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                actualizarItems();
            }
        });
        //FIN REFRESH
        //boton volver funcionalidad
        layoutbotonVolver = (RelativeLayout) findViewById(R.id.botonVolver);
        layoutbotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(PerfilActivity.this,PrincipalActivity.class);
                PerfilActivity.this.startActivity(intentRegP);
            }
        });
        //fin boton volver

        //boton editar servicios
        btnactualizarservicios = findViewById(R.id.btnActualizarServicios);
        btnactualizarservicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.layout_servicios, null);

                builder.setView(view);
                final AlertDialog dialogoeliminar = builder.show();
                //botones referenciados
                btn_guardarup = (Button) view.findViewById(R.id.Btn_GuardarUp);
                //fin referencias

                //funcion response
                pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
                final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                Response.Listener<String> responseListenerListaServicios = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            listaServiciosEmpresa = new ArrayList<>();
                            idrecycleractualizarservicios = view.findViewById(R.id.idRecyclerActualizarServicios);
                            idrecycleractualizarservicios.setLayoutManager(new LinearLayoutManager(PerfilActivity.this));
                            idrecycleractualizarservicios.setHasFixedSize(true);

                            JSONObject jsonReponse = new JSONObject(response);
                            Tay_servicioInner tay_servicioInner=null;
                            JSONArray json=jsonReponse.optJSONArray("usuario");
                            for (int i=0;i<json.length();i++){
                                tay_servicioInner=new Tay_servicioInner();
                                JSONObject jsonObject=null;
                                jsonObject=json.getJSONObject(i);

                                tay_servicioInner.setTay_servicio_nombre(jsonObject.optString("tay_servicio_nombre"));
                                tay_servicioInner.setTay_asoc_estado(jsonObject.optInt("tay_asoc_estado"));
                                tay_servicioInner.setTay_asoc_serv_id(jsonObject.optInt("tay_asoc_serv_id"));
                                tay_servicioInner.setTay_servicio_id(jsonObject.optInt("tay_servicio_id"));
                                tay_servicioInner.setSelected(true);
                                listaServiciosEmpresa.add(tay_servicioInner);
                            }
                            final Tay_servicioListaAdapter adapter=new Tay_servicioListaAdapter(listaServiciosEmpresa);
                            idrecycleractualizarservicios.setAdapter(adapter);
                            //boton guardar en el dialog
                            btn_guardarup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //sb=new StringBuffer();
                                    for (Tay_servicioInner ti: adapter.checkedServicios)
                                    {
                                        String comentario=ti.getTay_servicio_nombre();
                                        int estado=ti.getTay_asoc_estado();
                                        int servicio=ti.getTay_asoc_serv_id();
                                        if (servicio==0){
                                            //sb.append("AGREGAR SERVICIO "+comentario+"-"+estado+"-"+servicio);
                                            //sb.append("\n");
                                            int servicioser=ti.getTay_servicio_id();
                                            pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
                                            final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                            int estadoser=1;

                                            Response.Listener<String> respoListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonReponse = new JSONObject(response);
                                                        AlertDialog.Builder builder= new AlertDialog.Builder(PerfilActivity.this);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            RegistrarServicioAsocRequest registrarservicioasocRequest = new RegistrarServicioAsocRequest(servicioser,empresa,estadoser, respoListener);
                                            RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                                            queue.add(registrarservicioasocRequest);
                                        }else{
                                            if (!(estado==1)){
                                                //funcion ACTUALIZAR SERVICIO del response
                                                estado = 1;
                                                Response.Listener<String> responseListenerUpdateServicios = new Response.Listener<String>(){
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
                                                UpdateServiciosRequest updateserviciosRequest = new UpdateServiciosRequest(estado,servicio,responseListenerUpdateServicios);
                                                RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                                                queue.add(updateserviciosRequest);
                                                //fin funcion de l response
                                            }
                                        }
                                    }
                                    for (Tay_servicioInner ti: adapter.checkedServiciosEliminados)
                                    {
                                        String comentario=ti.getTay_servicio_nombre();
                                        int estado=ti.getTay_asoc_estado();
                                        int servicio=ti.getTay_asoc_serv_id();
                                        if(!(servicio==0)){
                                            if(!(estado==0)){
                                                //funcion ESTADO 0 SERVICIO del response
                                                estado = 0;
                                                Response.Listener<String> responseListenerUpdateServicios = new Response.Listener<String>(){
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
                                                UpdateServiciosRequest updateserviciosRequest = new UpdateServiciosRequest(estado,servicio,responseListenerUpdateServicios);
                                                RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                                                queue.add(updateserviciosRequest);
                                                //fin funcion de l response
                                            }
                                        }
                                    }
                                    dialogoeliminar.dismiss();
                                }
                            });
                            //fin boton guardar
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                ListarServiciosRequest listarserviciosRequest = new ListarServiciosRequest(empresa, responseListenerListaServicios);
                RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                queue.add(listarserviciosRequest);
                //fin funcion response

                //boton cancelar en el dialog
                Button btncancelarup = (Button) view.findViewById(R.id.Btn_CancelarUp);
                btncancelarup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogoeliminar.dismiss();
                    }
                });
                //fin boton cancelar


            }
        });
        //fin editar servicios

        //boton editar perfil informacion
        btnactualizarPerfil = findViewById(R.id.btnActualizarPerfil);
        btnactualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VARIABLES
                String direccionempresa = pref.getString("tay_empresa_direccion", "");
                String referenciaempresa = pref.getString("tay_empresa_referencia", "");
                String telefonoempresa = pref.getString("tay_empresa_telefono", "");
                final String horarioinicial = pref.getString("tay_empresa_horainicial", "");
                final String horariofinal = pref.getString("tay_empresa_horafin", "");
                //FIN VARIABLES

                final AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_perfil_informacion, null);
                builder.setView(view);
                final AlertDialog dialogoeliminar = builder.show();

                //SETEO DE VALORES
                final EditText direccionup = (EditText) view.findViewById(R.id.txtDireccion);
                direccionup.setText(direccionempresa);

                final EditText referenciaup = (EditText) view.findViewById(R.id.txtReferencia);
                referenciaup.setText(referenciaempresa);

                final EditText telefonoup = (EditText) view.findViewById(R.id.txtTelefono);
                telefonoup.setText(telefonoempresa);
                //FIN SETEO

                //VARIABLES BOTONES
                btn_guardarupperfil = (Button) view.findViewById(R.id.Btn_GuardarUpPerfil);
                Button btncancelarup = (Button) view.findViewById(R.id.Btn_CancelarUp);
                //FIN VARIABLE BOTON

                //VARIABLE BOTONES
                horaentrada = view.findViewById(R.id.textHorario);
                minutosentrada = view.findViewById(R.id.minutosEntrada);
                ampmentrada = view.findViewById(R.id.ampmEntrada);
                horasalida =view.findViewById(R.id.horaSalida);
                minutossalida = view.findViewById(R.id.minutosSalida);
                ampmsalida = view.findViewById(R.id.ampmSalida);
                //FIN VARIABLES BOTONES

                //INICIO BOTON AGREGAR
                btn_guardarupperfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //funcion ACTUALIZAR SERVICIO del response
                        pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
                        final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                        String direccion=direccionup.getText().toString();
                        String referencia=referenciaup.getText().toString();
                        String telefono=telefonoup.getText().toString();
                        String entrada;
                        String salida;
                        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        int tcomida = listaComidas.get(spinner.getSelectedItemPosition()).getTay_tipocomida_id();
                        if ((ampmentrada.getText().toString()).equals("AM")){
                            if ((horaentrada.getText().toString()).equals("12")){
                                entrada="00:"+minutosentrada.getText().toString();
                            }
                            else
                            {
                                entrada=horaentrada.getText().toString()+":"+minutosentrada.getText().toString();
                            }
                        }else{
                            if ((horaentrada.getText().toString()).equals("12")){
                                entrada="12:"+minutosentrada.getText().toString();
                            }
                            else
                            {
                                Integer horae = Integer.parseInt(horaentrada.getText().toString())+12;
                                entrada=horae+":"+minutosentrada.getText().toString();
                            }
                        }
                        if ((ampmsalida.getText().toString()).equals("AM")){
                            if ((horaentrada.getText().toString()).equals("12")){
                                salida="00:"+minutosentrada.getText().toString();
                            }
                            else {
                                salida = horasalida.getText().toString() + ":" + minutossalida.getText().toString();
                            }
                        }else{
                            if ((horaentrada.getText().toString()).equals("12")){
                                salida="12:"+minutosentrada.getText().toString();
                            }
                            else {
                                Integer horas = Integer.parseInt(horasalida.getText().toString()) + 12;
                                salida = horas + ":" + minutossalida.getText().toString();
                            }
                        }



                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("tay_empresa_tipocomida", tcomida);
                        editor.putString("tay_empresa_direccion", direccion);
                        editor.putString("tay_empresa_horainicial", entrada);
                        editor.putString("tay_empresa_horafin", salida);
                        editor.putString("tay_empresa_telefono", telefono);
                        editor.putString("tay_empresa_referencia", referencia);
                        editor.apply();

                        Response.Listener<String> responseListenerUpdateServicios = new Response.Listener<String>(){
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
                        UpdateEmpresaRequest updateempresaRequest = new UpdateEmpresaRequest(tcomida,direccion,referencia,telefono,entrada,salida,empresa,responseListenerUpdateServicios);
                        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                        queue.add(updateempresaRequest);
                        //fin funcion de l respons

                        dialogoeliminar.dismiss();
                    }
                });
                //FIN BOTON AGREGAR

                //SETEO DEL HORARIO
                try {
                    SimpleDateFormat _24Horas = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12Horas = new SimpleDateFormat("hh:mm a");
                    Date _24HorasInicialDt = _24Horas.parse(horarioinicial);
                    Date _24HorasFinalDt = _24Horas.parse(horariofinal);
                    horaentrada.setText((_12Horas.format(_24HorasInicialDt)).substring(0,2));
                    minutosentrada.setText((_12Horas.format(_24HorasInicialDt)).substring(3,5));
                    ampmentrada.setText((_12Horas.format(_24HorasInicialDt)).substring(6,8));
                    horasalida.setText((_12Horas.format(_24HorasFinalDt)).substring(0,2));
                    minutossalida.setText((_12Horas.format(_24HorasFinalDt)).substring(3,5));
                    ampmsalida.setText((_12Horas.format(_24HorasFinalDt)).substring(6,8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //boton cancelar en el dialog
                btncancelarup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogoeliminar.dismiss();
                    }
                });
                //fin boton cancelar

                //boton AM/PM en el dialog
                ampmentrada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ampmentrada.getText().toString().equals("AM")){
                            ampmentrada.setText("PM");
                        }
                        else
                        {
                            ampmentrada.setText("AM");
                        }
                    }
                });
                ampmsalida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ampmsalida.getText().toString().equals("AM")){
                            ampmsalida.setText("PM");
                        }
                        else
                        {
                            ampmsalida.setText("AM");
                        }
                    }
                });
                //fin boton AM/PM

                pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
                final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                Response.Listener<String> responseListenerListaComidas = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                            listaComidas = new ArrayList<>();

                            JSONObject jsonReponse = new JSONObject(response);
                            Tay_comida tay_comida=null;
                            JSONArray json=jsonReponse.optJSONArray("usuario");
                            for (int i=0;i<json.length();i++){
                                tay_comida=new Tay_comida();
                                JSONObject jsonObject=null;
                                jsonObject=json.getJSONObject(i);

                                tay_comida.setTay_tipocomida_id(Integer.parseInt(jsonObject.optString("tay_tipocomida_id")));
                                tay_comida.setTay_tipocomida_nombre(jsonObject.optString("tay_tipocomida_nombre"));
                                tay_comida.setTay_tipocomida_estado(Integer.parseInt(jsonObject.optString("tay_tipocomida_estado")));
                                tay_comida.setSelected(false);
                                listaComidas.add(tay_comida);
                            }
                            //MyAdapter myAdapter = new MyAdapter(PerfilActivity.this, 0,
                            //        listaServicios);
                            //spinner.setAdapter(myAdapter);
                            Tay_comidaListaAdapter adapter = new Tay_comidaListaAdapter(PerfilActivity.this,0,listaComidas);
                            spinner.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                ListarComidasRequest listarcomidasRequest = new ListarComidasRequest(empresa, responseListenerListaComidas);
                RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
                queue.add(listarcomidasRequest);
            }
        });
        //fin boton

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intentReg = new Intent(PerfilActivity.this,PrincipalActivity.class);
                        PerfilActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:
                        Intent intentReg2 = new Intent(PerfilActivity.this,ComidaPrincipalActivity.class);
                        PerfilActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(PerfilActivity.this,CartaActivity.class);
                        PerfilActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg4 = new Intent(PerfilActivity.this,OfertaActivity.class);
                        PerfilActivity.this.startActivity(intentReg4);
                        break;
                    case R.id.ic_perfil:

                        break;

                }
                return false;
            }
        });
        //comprobar si esta con permisos la fotografia
        if(solicitaPermisosVersionesSuperiores()){
            fotoperfil.setEnabled(true);
        }else{
            fotoperfil.setEnabled(false);
        }
        //fin comprobacion de permisos
        //funcionamiento del btndefoto
        fotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });
        //fin funcionamiento del boton
        obtenerInformación();
        obtenerServicios();
    }
    //ACTUALIZAR IMAGEN NUEVA
    private void actualizarfoto(){
        //funcion ACTUALIZAR SERVICIO del response
        pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
        final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        final String imagen=convertirImgString(bitmap);
        final String perfil=IMAGENES_URL+empresa+".jpg";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("tay_ruta_imagen", perfil);
        editor.apply();

        Response.Listener<String> responseListenerUpdateImagen = new Response.Listener<String>(){
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
        UpdateEmpresaImagenRequest updateempresaimagenRequest = new UpdateEmpresaImagenRequest(imagen,empresa,responseListenerUpdateImagen);
        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
        queue.add(updateempresaimagenRequest);
        //fin funcion de l respons
        AlertDialog.Builder builder= new AlertDialog.Builder(PerfilActivity.this);
        builder.setMessage("FOTO ACTUALIZADA")
                .setNegativeButton("Ok",null)
                .create().show();
    }
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
    //FIN ACTUALIZAR IMAGEN NUEVA

    //funcion para el funcionamiento del boton de foto
    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(PerfilActivity.this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }
    private void abriCamara() {
        File miFile=new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }

        if(isCreada==true){
            Long consecutivo= System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;//indicamos la ruta de almacenamiento

            fileImagen=new File(path);

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=PerfilActivity.this.getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(PerfilActivity.this,authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent,COD_FOTO);

            ////

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                perfilimagen.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(PerfilActivity.this.getContentResolver(),miPath);
                    perfilimagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(PerfilActivity.this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                perfilimagen.setImageBitmap(bitmap);
                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
        actualizarfoto();
    }
    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }


    }
    //fin funcionamiento del boton de foto
    //funccion pedir permisos
    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((PerfilActivity.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&PerfilActivity.this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(PerfilActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                fotoperfil.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PerfilActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",PerfilActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(PerfilActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PerfilActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
    //fin de la funcion de permisos

    private void obtenerInformación() {
        pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
        final String nombreempresa = pref.getString("tay_empresa_nombre", "");

        //variables
        String direccionempresa = pref.getString("tay_empresa_direccion", "");
        String referenciaempresa = pref.getString("tay_empresa_referencia", "");
        String telefonoempresa = pref.getString("tay_empresa_telefono", "");
        //fin variables

        nombreEmpresa = findViewById(R.id.NombrePerfil);
        nombreEmpresa.setText(nombreempresa);
        direccionEmpresa = findViewById(R.id.textoDireccion);
        direccionEmpresa.setText(direccionempresa+" Ref: "+referenciaempresa);
        telefonoEmpresa = findViewById(R.id.textoTelefono);
        telefonoEmpresa.setText(telefonoempresa);
        final String horarioinicial = pref.getString("tay_empresa_horainicial", "");
        final String horariofinal = pref.getString("tay_empresa_horafin", "");
        horarioEmpresa = findViewById(R.id.textoHorario);
        try {
            SimpleDateFormat _24Horas = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12Horas = new SimpleDateFormat("hh:mm a");
            Date _24HorasInicialDt = _24Horas.parse(horarioinicial);
            Date _24HorasFinalDt = _24Horas.parse(horariofinal);
            horarioEmpresa.setText("Horarios de atención: "+_12Horas.format(_24HorasInicialDt)+" - "+_12Horas.format(_24HorasFinalDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //obtener el id de la comida
        final int idcomida= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_tipocomida", 0)));
        final String urlimagen= pref.getString("tay_ruta_imagen", "");
        //fin id de comida
        if(isInternetOn()) {
            //obtener el tipo de comida
            Response.Listener<String> responseListenerTipoComida = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonReponse = new JSONObject(response);
                        boolean success = jsonReponse.getBoolean("success");

                        if (success){
                            String descripcion;
                            descripcion = jsonReponse.getString("tay_tipocomida_nombre");
                            textoComida =findViewById(R.id.textoComida);
                            textoComida.setText(descripcion);
                        }else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(PerfilActivity.this);
                            builder.setMessage("Error al obtener")
                                    .setNegativeButton("Retry",null)
                                    .create().show();
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };

            TipoComidaRequest tipocomidaRequest = new TipoComidaRequest(idcomida,responseListenerTipoComida);
            RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
            queue.add(tipocomidaRequest);
            //fin obtener tipo de comida
            //PASAR IMAGEN AL IMAGEVIEW
            String urlcompleta=APP_API_URL+urlimagen;
            if (!(urlimagen.equals("null"))){
                Picasso.with(getApplicationContext()).invalidate(urlcompleta);
                Picasso.with(getApplicationContext()).load(urlcompleta).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(perfilimagen);
                //Picasso.with(getApplicationContext()).load(urlcompleta).into(perfilimagen);
            }
            //FIN PASAR IMAGEN
        }
    }
    private void obtenerServicios() {
        pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaServicios = new ArrayList<>();
                    recyclerServicios = findViewById(R.id.idRecyclerServicios);
                    recyclerServicios.setLayoutManager(new LinearLayoutManager(PerfilActivity.this));
                    recyclerServicios.setHasFixedSize(true);

                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_servicio tay_servicio=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");
                    for (int i=0;i<json.length();i++){
                        tay_servicio=new Tay_servicio();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        tay_servicio.setTay_servicio_nombre(jsonObject.optString("tay_servicio_nombre"));
                        tay_servicio.setTay_servicio_id(Integer.parseInt(jsonObject.optString("tay_servicio_id")));
                        tay_servicio.setTay_servicio_imagenurl(jsonObject.optString("tay_servicio_imagenurl"));
                        listaServicios.add(tay_servicio);
                    }
                    Tay_servicioAdapter adapter=new Tay_servicioAdapter(PerfilActivity.this,listaServicios);
                    recyclerServicios.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        ServicioRequest servicioRequest = new ServicioRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
        queue.add(servicioRequest);
    }
    public void actualizarItems() {
        // shuffle the ArrayList's items and set the adapter
        //Collections.shuffle(listaServicios, new Random(System.currentTimeMillis()));
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        //obtener el id de la comida
        final int idcomida= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_tipocomida", 0)));
        //fin id de comida
        if(isInternetOn()) {
            //obtener el tipo de comida
            Response.Listener<String> responseListenerTipoComida = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonReponse = new JSONObject(response);
                        boolean success = jsonReponse.getBoolean("success");

                        if (success) {
                            String descripcion;
                            descripcion = jsonReponse.getString("tay_tipocomida_nombre");
                            textoComida = findViewById(R.id.textoComida);
                            textoComida.setText(descripcion);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                            builder.setMessage("Error al obtener")
                                    .setNegativeButton("Retry", null)
                                    .create().show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            TipoComidaRequest tipocomidaRequest = new TipoComidaRequest(idcomida, responseListenerTipoComida);
            RequestQueue queuetc = Volley.newRequestQueue(PerfilActivity.this);
            queuetc.add(tipocomidaRequest);
        }

        final String urlimagen= pref.getString("tay_ruta_imagen", "");
        //PASAR IMAGEN AL IMAGEVIEW
        String urlcompleta=APP_API_URL+urlimagen;
        if (!(urlimagen.equals("null"))){
            Picasso.with(getApplicationContext()).invalidate(urlcompleta);
            Picasso.with(getApplicationContext()).load(urlcompleta).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(perfilimagen);
            //Picasso.with(getApplicationContext()).load(urlcompleta).into(perfilimagen);
        }
        //FIN PASAR IMAGEN

        //ACTUALIZAR CAJAS DE TEXTO
        //variables
        String direccionempresa = pref.getString("tay_empresa_direccion", "");
        String referenciaempresa = pref.getString("tay_empresa_referencia", "");
        String telefonoempresa = pref.getString("tay_empresa_telefono", "");
        //fin variables
        direccionEmpresa = findViewById(R.id.textoDireccion);
        direccionEmpresa.setText(direccionempresa+" Ref: "+referenciaempresa);
        telefonoEmpresa = findViewById(R.id.textoTelefono);
        telefonoEmpresa.setText(telefonoempresa);
        final String horarioinicial = pref.getString("tay_empresa_horainicial", "");
        final String horariofinal = pref.getString("tay_empresa_horafin", "");
        horarioEmpresa = findViewById(R.id.textoHorario);
        try {
            SimpleDateFormat _24Horas = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12Horas = new SimpleDateFormat("hh:mm a");
            Date _24HorasInicialDt = _24Horas.parse(horarioinicial);
            Date _24HorasFinalDt = _24Horas.parse(horariofinal);
            horarioEmpresa.setText("Horarios de atención: "+_12Horas.format(_24HorasInicialDt)+" - "+_12Horas.format(_24HorasFinalDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //FIN ACTUALIZAR CAJAS DE TEXTO

        pref = PreferenceManager.getDefaultSharedPreferences(PerfilActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaServicios = new ArrayList<>();
                    recyclerServicios = findViewById(R.id.idRecyclerServicios);
                    recyclerServicios.setLayoutManager(new LinearLayoutManager(PerfilActivity.this));
                    recyclerServicios.setHasFixedSize(true);
                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_servicio tay_servicio=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");
                        for (int i=0;i<json.length();i++){
                            tay_servicio=new Tay_servicio();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);

                            tay_servicio.setTay_servicio_nombre(jsonObject.optString("tay_servicio_nombre"));
                            tay_servicio.setTay_servicio_id(Integer.parseInt(jsonObject.optString("tay_servicio_id")));
                            tay_servicio.setTay_servicio_imagenurl(jsonObject.optString("tay_servicio_imagenurl"));
                            listaServicios.add(tay_servicio);
                        }
                        Tay_servicioAdapter adapter=new Tay_servicioAdapter(PerfilActivity.this,listaServicios);
                        recyclerServicios.setAdapter(adapter);
                        recyclerServicios.setVisibility(View.VISIBLE);

                }catch (JSONException e){
                    e.printStackTrace();
                    recyclerServicios.setVisibility(View.INVISIBLE);
                }
            }
        };
        ServicioRequest servicioRequest = new ServicioRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);
        queue.add(servicioRequest);
    }
    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) PerfilActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
