package pe.oranch.restaurantroky.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Daniel on 02/11/2017.
 */

public class ComidaPrincipalActivity extends AppCompatActivity{
    //BOTONES PRINCIPALES DENTRO DEL CONTEXTO
    Button btn_menu_texto;
    Button btn_subir_menu_foto;
    //FIN BOTONES PRINCIPALES Y SU CONTEXTO

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida_principal);

        //IMPLEMENTACION Y SETEO DE LAS VARIABLES EN SUS RESPECTIVOS BOTONES
        btn_menu_texto = (Button) findViewById(R.id.Btn_subir_menu_texto);
        btn_menu_texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComidaPrincipalActivity.this,ComidaActivity.class);
                ComidaPrincipalActivity.this.startActivity(intent);
            }
        });
        btn_subir_menu_foto = (Button) findViewById(R.id.Btn_subir_menu_foto);
        //funcionamiento del btndefoto
        btn_subir_menu_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComidaPrincipalActivity.this,ComidaFotoActivity.class);
                ComidaPrincipalActivity.this.startActivity(intent);
            }
        });
        //fin funcionamiento del boton
        //FIN IMPLEMENTACION Y SETEO DE LOS BOTONES

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
                        Intent intentReg = new Intent(ComidaPrincipalActivity.this,PrincipalActivity.class);
                        ComidaPrincipalActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_menu:

                        break;
                    case R.id.ic_carta:
                        Intent intentReg2 = new Intent(ComidaPrincipalActivity.this,CartaActivity.class);
                        ComidaPrincipalActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg3 = new Intent(ComidaPrincipalActivity.this,OfertaActivity.class);
                        ComidaPrincipalActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg4 = new Intent(ComidaPrincipalActivity.this,PerfilActivity.class);
                        ComidaPrincipalActivity.this.startActivity(intentReg4);
                        break;

                }
                return false;
            }
        });
    }
}
