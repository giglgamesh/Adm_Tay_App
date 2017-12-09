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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.request.RegistrarMenuFotoRequest;
import pe.oranch.restaurantroky.request.RegistrarMenuRequest;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Daniel on 02/11/2017.
 */

public class ComidaFotoActivity extends AppCompatActivity{
    //variable boton volver
    RelativeLayout layoutbotonVolver;
    RelativeLayout rellayoutpreciomenos, rellayoutpreciomas;
    Double preciot;
    //fin variable botron volver

    //BOTON REGISTRAR FOTO MENU
    Button btn_subir_carta;
    EditText edittextprecio;
    //FIN BOTON

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion


    //variable para la fecha
    Date fechayhora=new Date();
    //fin variable para la fecha actual

    //VARIABLES PARA LA FOTOGRAFIA
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    //almacenamiento de imagenes
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/menu/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes/menu";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;
    ImageView imgfoto;
    //fin almacenamiento de imagenes
    //FIN VARIABLES

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida_foto);

        //INICIAR IMAGEVIEW
        imgfoto = (ImageView) findViewById(R.id.imgFoto);
        //FIN INICIALIZAR IMAGEVIEW

        //INICIAR BOTON
        btn_subir_carta = (Button) findViewById(R.id.Btn_subir_carta);
        edittextprecio = (EditText) findViewById(R.id.editTextPrecio);
        btn_subir_carta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarMenuFoto();
            }
        });
        //FIN INICIAR BOTON

        //boton volver funcionalidad
        layoutbotonVolver = (RelativeLayout) findViewById(R.id.botonVolver);
        layoutbotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(ComidaFotoActivity.this,ComidaPrincipalActivity.class);
                ComidaFotoActivity.this.startActivity(intentRegP);
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
                    edittextprecio.setText(Double.toString(preciot-1));
                }
            }
        });
        //FIN FUNCIONALIDAD

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intentReg = new Intent(ComidaFotoActivity.this,PrincipalActivity.class);
                        ComidaFotoActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:

                        break;
                    case R.id.ic_carta:
                        Intent intentReg2 = new Intent(ComidaFotoActivity.this,CartaActivity.class);
                        ComidaFotoActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg3 = new Intent(ComidaFotoActivity.this,OfertaActivity.class);
                        ComidaFotoActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg4 = new Intent(ComidaFotoActivity.this,PerfilActivity.class);
                        ComidaFotoActivity.this.startActivity(intentReg4);
                        break;

                }
                return false;
            }
        });
        //comprobar si esta con permisos la fotografia
        if(solicitaPermisosVersionesSuperiores()){
            mostrarDialogOpciones();
        }else{
            Intent intent = new Intent(ComidaFotoActivity.this,ComidaPrincipalActivity.class);
            ComidaFotoActivity.this.startActivity(intent);
        }
        //fin comprobacion de permisos
    }
    //funcion para el funcionamiento del boton de foto
    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(ComidaFotoActivity.this);
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
                        Intent intent = new Intent(ComidaFotoActivity.this,ComidaPrincipalActivity.class);
                        ComidaFotoActivity.this.startActivity(intent);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=ComidaFotoActivity.this.getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(ComidaFotoActivity.this,authorities,fileImagen);
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
        Intent intent = new Intent(ComidaFotoActivity.this,ComidaFotoActivity.class);
        switch (requestCode){
            case COD_SELECCIONA:
                //DECLARACION INTENT
                Uri miPath=data.getData();
                int variable_selecciona=1;
                intent.putExtra("imagen_miPath", miPath);
                intent.putExtra("variable_selecciona", variable_selecciona);
                imgfoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(ComidaFotoActivity.this.getContentResolver(),miPath);
                    //enviar valor
                    intent.putExtra("imagen_bitmap", bitmap);
                    //fin envio de valor
                    imgfoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(ComidaFotoActivity.this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                intent.putExtra("imagen_bitmap", bitmap);
                imgfoto.setImageBitmap(bitmap);
                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
        //ComidaPrincipalActivity.this.startActivity(intent);
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
        if((ComidaFotoActivity.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&ComidaFotoActivity.this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
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
                Toast.makeText(ComidaFotoActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                //btn_subir_menu_foto.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ComidaFotoActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",ComidaFotoActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(ComidaFotoActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ComidaFotoActivity.this);
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
    //METODO PARA REGISTRAR MENU CON FOTO
    private void RegistrarMenuFoto(){
        if (inputValidation()){
                //VARIABLES PARA EL REGISTRO
                pref = PreferenceManager.getDefaultSharedPreferences(ComidaFotoActivity.this.getBaseContext());
                final int empresa=Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
                final String empresanombre = String.valueOf(pref.getString("tay_empresa_nombre",""));

                String fecha = null;
                fecha=(fechayhora.getYear()+1900)+"-"+(fechayhora.getMonth()+1)+"-"+fechayhora.getDate()+" "+fechayhora.getHours()+":"+fechayhora.getMinutes()+":"+fechayhora.getSeconds();

                String fechanombre=null;
                fechanombre=(fechayhora.getYear()+1900)+""+(fechayhora.getMonth()+1)+""+fechayhora.getDate()+""+fechayhora.getHours()+""+fechayhora.getMinutes()+""+fechayhora.getSeconds()+"";

                String nombre=null;
                nombre = empresanombre+fechanombre;

                int estado = 1;

                Double precio = 0.00;
                precio = Double.parseDouble(edittextprecio.getText().toString());

                final String imagen=convertirImgString(bitmap);

                Response.Listener<String> responseListenerRegistrarMenuFoto = new Response.Listener<String>(){
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
                RegistrarMenuFotoRequest registrarmenufotoRequest = new RegistrarMenuFotoRequest(nombre,estado,fecha,precio,empresa,imagen,responseListenerRegistrarMenuFoto);
                RequestQueue queue = Volley.newRequestQueue(ComidaFotoActivity.this);
                queue.add(registrarmenufotoRequest);
                //FIN REGISTRO DE LA ENTRADA
                AlertDialog.Builder builder= new AlertDialog.Builder(ComidaFotoActivity.this);
                builder.setMessage("REGISTRO DE MENU CON EXITO")
                        .create().show();
                Intent intent = new Intent(ComidaFotoActivity.this,ComidaPrincipalActivity.class);
                ComidaFotoActivity.this.startActivity(intent);
        }
    }
    //FIN DEL METODO DE REGISTRO
    //VALIDACION DE CAMPOS
    private boolean inputValidation() {
        if(edittextprecio.getText().toString().equals("")) {
            AlertDialog.Builder builder= new AlertDialog.Builder(ComidaFotoActivity.this);
            builder.setMessage("NO HAS INGRESADO UN PRECIO")
                    .setNegativeButton("Volver a Intentar",null)
                    .create().show();
            return false;
        }
        return true;
    }
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
    //FIN VALIDACION CAMPOS
}
