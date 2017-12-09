package pe.oranch.restaurantroky.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;

/**
 * Created by Daniel on 02/11/2017.
 */

public class PersonaActivity extends AppCompatActivity{
    RelativeLayout layoutbotonVolver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);
        //boton volver funcionalidad
        layoutbotonVolver = (RelativeLayout) findViewById(R.id.botonVolver);
        layoutbotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegP = new Intent(PersonaActivity.this,PrincipalActivity.class);
                PersonaActivity.this.startActivity(intentRegP);
            }
        });
        //fin boton volver

        TextView title = (TextView) findViewById(R.id.activityTextPersonasCerca);

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
                        Intent intentReg2 = new Intent(PersonaActivity.this,ComidaPrincipalActivity.class);
                        PersonaActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(PersonaActivity.this,CartaActivity.class);
                        PersonaActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg4 = new Intent(PersonaActivity.this,OfertaActivity.class);
                        PersonaActivity.this.startActivity(intentReg4);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg5 = new Intent(PersonaActivity.this,PerfilActivity.class);
                        PersonaActivity.this.startActivity(intentReg5);
                        break;

                }
                return false;
            }
        });
    }
}
