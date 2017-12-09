package pe.oranch.restaurantroky.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.Tay_cartaAdapter;
import pe.oranch.restaurantroky.adapter.Tay_entradasAdapter;
import pe.oranch.restaurantroky.entidades.Tay_carta;
import pe.oranch.restaurantroky.entidades.Tay_menu;
import pe.oranch.restaurantroky.request.ObtenerCartaRequest;
import pe.oranch.restaurantroky.request.ObtenerEntradasRequest;
import pe.oranch.restaurantroky.request.RegisterRequest;
import pe.oranch.restaurantroky.request.RegistrarCartaRequest;
import pe.oranch.restaurantroky.request.RegistrarMenuRequest;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Daniel on 02/11/2017.
 */

public class CartaActivity extends AppCompatActivity{
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    ScrollView scrollview01;
    RelativeLayout rellayoutpreciomenos, rellayoutpreciomas;
    Double preciot;
    //FIN REFRESH LAYOUT

    ImageView imageview;
    EditText edittext;

    //Variables para el funcionamiento
    Button btn_agregar_plato;
    EditText txtplatocarta, edittextprecio;
    ImageView fotocarta;
    ImageView imgfoto;
    int variabledeagregar=0;
    int variabledemostrar=0;
    Integer habilitadoredittex=0;
    Integer habilitador=0;
    Integer metodo = 0;
    //variable boton subir
    Button btnsubircarta;
    //fin boton subir

        //variable del archivo de sesion
        private SharedPreferences pref;
        //fin archivo de sesion

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    String cajadetextoentrada=null;

        //almacenamiento de imagenes
        private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
        private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
        private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
        private String path;//almacena la ruta de la imagen
        //fin almacenamiento de imagenes

    File fileImagen;
    Bitmap bitmap;
    ArrayList<Tay_carta> listaCartas;
    ArrayList<Tay_carta> listaCartasEspeciales;
    RecyclerView recyclercarta;
    //fin variables funcionamiento
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);

        TextView title = (TextView) findViewById(R.id.activitySubTitulo);
        //Iniciador campos principales para el funcionamiento
        fotocarta = findViewById(R.id.fotoCarta);
        imgfoto = findViewById(R.id.imgFoto);
        txtplatocarta = (EditText) findViewById(R.id.txtPlatoCarta);
        edittextprecio = (EditText) findViewById(R.id.editTextPrecio);
        btn_agregar_plato=(Button) findViewById(R.id.Btn_agregar_plato);
        //fin iniciador de campos principales
        //enlazando el boton a la variable
        btnsubircarta = (Button) findViewById(R.id.Btn_subir_carta);
        //fin enlase

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(2);
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
                        Intent intentReg = new Intent(CartaActivity.this,PrincipalActivity.class);
                        CartaActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:
                        Intent intentReg2 = new Intent(CartaActivity.this,ComidaPrincipalActivity.class);
                        CartaActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:

                        break;
                    case R.id.ic_oferta:
                        Intent intentReg3 = new Intent(CartaActivity.this,OfertaActivity.class);
                        CartaActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg4 = new Intent(CartaActivity.this,PerfilActivity.class);
                        CartaActivity.this.startActivity(intentReg4);
                        break;

                }
                return false;
            }
        });
        //metodo listar carta
        mostrarcarta();
        //fin listar carta
        //comprobar si esta con permisos la fotografia
        if(solicitaPermisosVersionesSuperiores()){
            fotocarta.setEnabled(true);
        }else{
            fotocarta.setEnabled(false);
        }
        //fin comprobacion de permisos
        //funcionamiento del btndefoto
        fotocarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });
        //fin funcionamiento del boton

    }
    private void carta(){
        pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaCartas = new ArrayList<>();
                    recyclercarta = findViewById(R.id.idRecyclerServicios);
                    recyclercarta.setLayoutManager(new LinearLayoutManager(CartaActivity.this));
                    recyclercarta.setHasFixedSize(true);

                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_carta tay_carta=null;
                    Tay_carta tay_cartaespecial=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");
                    for (int i=0;i<(json.length());i++){
                        tay_cartaespecial=new Tay_carta();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);
                        tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                        tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                        tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                        listaCartas.add(tay_cartaespecial);
                    }
                        Tay_cartaAdapter adapter=new Tay_cartaAdapter(CartaActivity.this,listaCartas);
                        recyclercarta.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                    habilitador=1;
                }
            }
        };
        ObtenerCartaRequest obtenercartaRequest = new ObtenerCartaRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
        queue.add(obtenercartaRequest);
    }
    //funcion para obtener cartas
    private void mostrarcarta(){
        pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaCartas = new ArrayList<>();
                    listaCartasEspeciales = new ArrayList<>();
                    recyclercarta = findViewById(R.id.idRecyclerServicios);
                    recyclercarta.setLayoutManager(new LinearLayoutManager(CartaActivity.this));
                    recyclercarta.setHasFixedSize(true);
                    //recyclercarta.setNestedScrollingEnabled(false);
                    //FUNCIONAMIENTO SUBIR CARTA
                    btnsubircarta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (habilitador==0){
                                AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                builder.setMessage("NO HAY NUEVOS DATOS PARA REGISTRAR")
                                        .setNegativeButton("Ok",null)
                                        .create().show();
                            }else {
                                if (metodo == 1){
                                    if (inputValidationCarta()){
                                        if (habilitadoredittex==2){
                                            AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                            builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }else{
                                            //Toast.makeText(ComidaActivity.this, "COMPROBAR Y REGISTRAR ENTRADA:" + (listaEntradas.size() + listaSegundos.size()), Toast.LENGTH_SHORT).show();
                                            //VARIABLES PARA EL REGISTRO
                                            final String nombre=txtplatocarta.getText().toString();
                                            final double precio=Double.parseDouble(String.valueOf(edittextprecio.getText().toString()));
                                            pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
                                            final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                            final String imagen=convertirImgString(bitmap);

                                            Response.Listener<String> respoListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonReponse = new JSONObject(response);
                                                        AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            RegistrarCartaRequest registrarcartaRequest = new RegistrarCartaRequest(nombre,precio,empresa,imagen, respoListener);
                                            RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
                                            queue.add(registrarcartaRequest);
                                            //FIN REGISTRO DE LA ENTRADA
                                            habilitadoredittex=2;
                                            AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                            builder.setMessage("REGISTRO DE PLATO CON EXITO")
                                                    .setNegativeButton("Ok",null)
                                                    .create().show();
                                        }
                                    }
                                }
                                else if(metodo == 0){
                                    if (inputValidationCarta()){
                                        final String nombre=txtplatocarta.getText().toString();
                                        final double precio=Double.parseDouble(String.valueOf(edittextprecio.getText().toString()));
                                        pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
                                        final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                                        final String imagen=convertirImgString(bitmap);

                                        Response.Listener<String> respoListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonReponse = new JSONObject(response);
                                                    AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        RegistrarCartaRequest registrarcartaRequest = new RegistrarCartaRequest(nombre,precio,empresa,imagen, respoListener);
                                        RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
                                        queue.add(registrarcartaRequest);
                                        habilitador=0;
                                        habilitadoredittex=2;
                                        AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                        builder.setMessage("REGISTRO CON EXITO")
                                                .setNegativeButton("Ok",null)
                                                .create().show();
                                    }
                                }
                            }
                        }
                    });
                    //FIN FUNCIONAMIENTO SUBIR CARTA
                    //funcionamiento del btnagregar
                    btn_agregar_plato.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cajadetextoentrada = txtplatocarta.getText().toString();
                            if (cajadetextoentrada.matches("") & habilitador==0)
                            {
                                Toast.makeText(CartaActivity.this, "CAMPO PRINCIPAL VACIO", Toast.LENGTH_SHORT).show();
                                //registrarcarta();
                            }
                            else{
                                if (habilitadoredittex==0){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                    builder.setMessage("SUBIDA DE PLATO EN PROCESO")
                                            .setNegativeButton("Ok",null)
                                            .create().show();
                                }else if(habilitadoredittex==1){
                                    //Toast.makeText(ComidaActivity.this,"METODO DE AGREGADO"+addmetodo,Toast.LENGTH_SHORT).show();
                                    //PONER EL CAMPO VACIO Y ACTUALIZA
                                    //Tay_cartaAdapter adapter=new Tay_cartaAdapter(CartaActivity.this,listaCartas);
                                    //recyclercarta.setAdapter(adapter);
                                    carta();
                                    txtplatocarta.setText("");
                                    edittextprecio.setText("");
                                    //FIN ACTUALIZAR Y PONER EL CAMPO VACIO
                                    habilitadoredittex=0;
                                    habilitador=1;
                                    metodo=1;
                                } else if (habilitadoredittex==2){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
                                    builder.setMessage("REFRESCA LA PAGINA PARA CONTINUAR")
                                            .setNegativeButton("Ok",null)
                                            .create().show();
                                }
                                //if (variabledeagregar==1){
                                //    variabledemostrar=1;
                                //    txtplatocarta.setText("");
                                //    edittextprecio.setText("");
                                //    btn_agregar_plato.setText("SUBIR");
                                //    variabledeagregar=0;
                                //}
                                //else{
                                //    registrarcarta();
                                    //variabledeagregar=1;
                                    //Intent intent = new Intent(CartaActivity.this,PrincipalActivity.class);
                                    //CartaActivity.this.startActivity(intent);
                                    //btn_agregar_plato.setText("AGREGAR");
                                //}
                            }
                        }
                    });
                    //fin funcionamiento del boton

                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_carta tay_carta=null;
                        Tay_carta tay_cartaespecial=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");
                            for (int i=0;i<(json.length());i++){
                                tay_cartaespecial=new Tay_carta();
                               JSONObject jsonObject=null;
                               jsonObject=json.getJSONObject(i);
                                tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                                tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                                tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                                listaCartas.add(tay_cartaespecial);
                            }
                        if (json.length()==0){
                            habilitador=1;
                            //if(variabledemostrar==1){
                            //    for (int i=0;i<(json.length());i++){
                            //        tay_cartaespecial=new Tay_carta();
                            //        JSONObject jsonObject=null;
                            //       jsonObject=json.getJSONObject(i);
                            //        tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                            //        tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                            //        tay_cartaespecial.setTay_carta_precio(jsonObject.optInt("tay_carta_precio"));
                            //        listaCartas.add(tay_cartaespecial);
                            //    }
                            //    Tay_cartaAdapter adapter=new Tay_cartaAdapter(listaCartas);
                            //    recyclercarta.setAdapter(adapter);
                            //    variabledeagregar=0;
                            //}
                            //else
                            //{
                            //
                            //}
                            //variabledemostrar=0;
                            //COMENTADO LOS DATOS Y AUMENTE EL HABILITADOR
                        }
                        else{
                            habilitador=0;
                            habilitadoredittex=1;
                            JSONObject jsonObjectespecial=null;
                            jsonObjectespecial=json.getJSONObject((json.length()-1));
                            txtplatocarta.setText(jsonObjectespecial.optString("tay_carta_nombre"));
                            edittextprecio.setText(String.valueOf(jsonObjectespecial.optDouble("tay_carta_precio")));
                            for (int i=0;i<(json.length()-1);i++){
                                tay_cartaespecial=new Tay_carta();
                                JSONObject jsonObject=null;
                                jsonObject=json.getJSONObject(i);
                                tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                                tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                                tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                                listaCartasEspeciales.add(tay_cartaespecial);
                            }
                            Tay_cartaAdapter adapter=new Tay_cartaAdapter(CartaActivity.this,listaCartasEspeciales);
                            recyclercarta.setAdapter(adapter);
                            variabledeagregar=1;
                            btn_agregar_plato.setText("AGREGAR");
                        }
                }catch (JSONException e){
                    e.printStackTrace();
                    habilitador=1;
                }
            }
        };
        ObtenerCartaRequest obtenercartaRequest = new ObtenerCartaRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
        queue.add(obtenercartaRequest);
    }
    //fin funcion obtener cartas
    //funcion para el registrar carta
    //private void registrarcarta(){
    //    if (inputValidation()){
    //        final String nombre=txtplatocarta.getText().toString();
    //        final int precio=Integer.parseInt(String.valueOf(edittextprecio.getText().toString()));
    //        pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
    //        final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
    //        final String imagen=convertirImgString(bitmap);
    //
    //        Response.Listener<String> respoListener = new Response.Listener<String>() {
    //            @Override
    //            public void onResponse(String response) {
    //                try {
    //                    JSONObject jsonReponse = new JSONObject(response);
    //                    AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        };
    //
    //        RegistrarCartaRequest registrarcartaRequest = new RegistrarCartaRequest(nombre,precio,empresa,imagen, respoListener);
    //        RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
                    //       queue.add(registrarcartaRequest);
    //        variabledeagregar = 1;
    //        btn_agregar_plato.setText("AGREGAR");
    //        AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
    //        builder.setMessage("REGISTRADO CON EXITO")
    //                .setNegativeButton("Ok",null)
    //                .create().show();
    //    }
    //    else {
    //        variabledeagregar = 0;
    //    }
    //}
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
    //fin funcion para el registrar carta
    //funcion para el funcionamiento del boton de foto
    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(CartaActivity.this);
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
                String authorities=CartaActivity.this.getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(CartaActivity.this,authorities,fileImagen);
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
                imgfoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(CartaActivity.this.getContentResolver(),miPath);
                    imgfoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(CartaActivity.this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                imgfoto.setImageBitmap(bitmap);

                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
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
        if((CartaActivity.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&CartaActivity.this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
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
                Toast.makeText(CartaActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                fotocarta.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(CartaActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",CartaActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(CartaActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(CartaActivity.this);
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
    private boolean inputValidationCarta() {

        if(txtplatocarta.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
            builder.setMessage("NO HAS INGRESADO UN PLATO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        if(edittextprecio.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(CartaActivity.this);
            builder.setMessage("NO HAS INGRESADO UN PRECIO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        return true;
    }
    //FUNCION PARA ACTUALIZAR EL SWIPEREFRESH
    private void actualizarItems(){
        if (habilitador==0){
            pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
            final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
            Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        listaCartas = new ArrayList<>();
                        listaCartasEspeciales = new ArrayList<>();
                        recyclercarta = findViewById(R.id.idRecyclerServicios);
                        recyclercarta.setLayoutManager(new LinearLayoutManager(CartaActivity.this));
                        recyclercarta.setHasFixedSize(true);

                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_carta tay_carta=null;
                        Tay_carta tay_cartaespecial=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");
                        for (int i=0;i<(json.length());i++){
                            tay_cartaespecial=new Tay_carta();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);
                            tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                            tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                            tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                            listaCartas.add(tay_cartaespecial);
                        }
                        if (json.length()==0){
                            //habilitador=1;
                        }
                        else{
                            //habilitador=0;
                            //habilitadoredittex=1;
                            JSONObject jsonObjectespecial=null;
                            jsonObjectespecial=json.getJSONObject((json.length()-1));
                            txtplatocarta.setText(jsonObjectespecial.optString("tay_carta_nombre"));
                            edittextprecio.setText(String.valueOf(jsonObjectespecial.optInt("tay_carta_precio")));
                            for (int i=0;i<(json.length()-1);i++){
                                tay_cartaespecial=new Tay_carta();
                                JSONObject jsonObject=null;
                                jsonObject=json.getJSONObject(i);
                                tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                                tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                                tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                                listaCartasEspeciales.add(tay_cartaespecial);
                            }
                            Tay_cartaAdapter adapter=new Tay_cartaAdapter(CartaActivity.this,listaCartasEspeciales);
                            recyclercarta.setAdapter(adapter);
                            btn_agregar_plato.setText("AGREGAR");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        habilitador=1;
                    }
                }
            };
            ObtenerCartaRequest obtenercartaRequest = new ObtenerCartaRequest(empresa,responseListenerLista);
            RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
            queue.add(obtenercartaRequest);
            habilitadoredittex=1;
        }else if (habilitador==1){
            pref = PreferenceManager.getDefaultSharedPreferences(CartaActivity.this.getBaseContext());
            final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
            Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        listaCartas = new ArrayList<>();
                        listaCartasEspeciales = new ArrayList<>();
                        recyclercarta = findViewById(R.id.idRecyclerServicios);
                        recyclercarta.setLayoutManager(new LinearLayoutManager(CartaActivity.this));
                        recyclercarta.setHasFixedSize(true);

                        JSONObject jsonReponse = new JSONObject(response);
                        Tay_carta tay_carta=null;
                        Tay_carta tay_cartaespecial=null;
                        JSONArray json=jsonReponse.optJSONArray("usuario");
                        for (int i=0;i<(json.length());i++){
                            tay_cartaespecial=new Tay_carta();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);
                            tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                            tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                            tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                            listaCartas.add(tay_cartaespecial);
                        }
                        if (json.length()==0){
                            habilitador=1;
                        }
                        else{
                            habilitador=0;
                            habilitadoredittex=1;
                            JSONObject jsonObjectespecial=null;
                            jsonObjectespecial=json.getJSONObject((json.length()-1));
                            txtplatocarta.setText(jsonObjectespecial.optString("tay_carta_nombre"));
                            edittextprecio.setText(String.valueOf(jsonObjectespecial.optDouble("tay_carta_precio")));
                            for (int i=0;i<(json.length()-1);i++){
                                tay_cartaespecial=new Tay_carta();
                                JSONObject jsonObject=null;
                                jsonObject=json.getJSONObject(i);
                                tay_cartaespecial.setTay_carta_id(jsonObject.optInt("tay_carta_id"));
                                tay_cartaespecial.setTay_carta_nombre(jsonObject.optString("tay_carta_nombre"));
                                tay_cartaespecial.setTay_carta_precio(jsonObject.optDouble("tay_carta_precio"));
                                listaCartasEspeciales.add(tay_cartaespecial);
                            }
                            Tay_cartaAdapter adapter=new Tay_cartaAdapter(CartaActivity.this,listaCartasEspeciales);
                            recyclercarta.setAdapter(adapter);
                            btn_agregar_plato.setText("AGREGAR");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        habilitador=1;
                    }
                }
            };
            ObtenerCartaRequest obtenercartaRequest = new ObtenerCartaRequest(empresa,responseListenerLista);
            RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
            queue.add(obtenercartaRequest);
        }
    }
    //FIN FUNCION PARA ACTUALIZAR EL SWIPEREFRESH
}
