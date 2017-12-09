package pe.oranch.restaurantroky.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.Comentario_innerjoin_clienteAdapter;
import pe.oranch.restaurantroky.entidades.Comentario_innerjoin_cliente;
import pe.oranch.restaurantroky.request.ListarComentariosRequest;
import pe.oranch.restaurantroky.request.UpdateComentariosRequest;

/**
 * Created by Daniel on 02/11/2017.
 */

public class ComentariosActivity extends AppCompatActivity{
    RelativeLayout layoutbotonVolver;

    //variable para el boton aprobar
    Button btn_aprobar,btn_eliminar;
    StringBuffer sb=null;
    //fin variable de boton

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion

    ArrayList<Comentario_innerjoin_cliente> listaComentario;
    RecyclerView recyclerComentarios;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        //check funcionalidad
        btn_aprobar = (Button) findViewById(R.id.Btn_Aprobar);
        btn_eliminar = (Button) findViewById(R.id.Btn_Eliminar);
        //fin check funcionalidad

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
                Intent intentRegP = new Intent(ComentariosActivity.this,PrincipalActivity.class);
                ComentariosActivity.this.startActivity(intentRegP);
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
                        Intent intentReg2 = new Intent(ComentariosActivity.this,ComidaPrincipalActivity.class);
                        ComentariosActivity.this.startActivity(intentReg2);
                        break;
                    case R.id.ic_carta:
                        Intent intentReg3 = new Intent(ComentariosActivity.this,CartaActivity.class);
                        ComentariosActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_oferta:
                        Intent intentReg4 = new Intent(ComentariosActivity.this,OfertaActivity.class);
                        ComentariosActivity.this.startActivity(intentReg4);
                        break;
                    case R.id.ic_perfil:
                        Intent intentReg5 = new Intent(ComentariosActivity.this,PerfilActivity.class);
                        ComentariosActivity.this.startActivity(intentReg5);
                        break;
                }
                return false;
            }
        });
        obtenercomentarios();
    }
    private void obtenercomentarios (){
        pref = PreferenceManager.getDefaultSharedPreferences(ComentariosActivity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));
        //funcion del response
        Response.Listener<String> responseListenerComentarios = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaComentario = new ArrayList<>();
                    recyclerComentarios = findViewById(R.id.idRecyclerComentarios);
                    recyclerComentarios.setLayoutManager(new LinearLayoutManager(ComentariosActivity.this));
                    recyclerComentarios.setHasFixedSize(true);

                    JSONObject jsonReponse = new JSONObject(response);
                    Comentario_innerjoin_cliente comentario_innerjoin_cliente=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");
                    for (int i=0;i<json.length();i++){
                        comentario_innerjoin_cliente=new Comentario_innerjoin_cliente();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        comentario_innerjoin_cliente.setTay_cliente_nombre(jsonObject.optString("tay_cliente_nombre"));
                        comentario_innerjoin_cliente.setTay_comentario_descripcion(jsonObject.optString("tay_comentario_descripcion"));
                        comentario_innerjoin_cliente.setTay_comentario_id(jsonObject.optInt("tay_comentario_id"));
                        comentario_innerjoin_cliente.setSelected(false);
                        listaComentario.add(comentario_innerjoin_cliente);
                    }
                    final Comentario_innerjoin_clienteAdapter adapter=new Comentario_innerjoin_clienteAdapter(listaComentario);
                    recyclerComentarios.setAdapter(adapter);
                    //Inicio funcionalidad del boton aprobar
                    btn_aprobar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb=new StringBuffer();
                            for (Comentario_innerjoin_cliente co: adapter.checkedComentarios)
                            {
                                final int estado = 1;
                                final int comentario=co.getTay_comentario_id();
                                sb.append("Comentario "+comentario+" aprobado");
                                sb.append("\n");
                                //funcion del response
                                Response.Listener<String> responseListenerUpdateComentarios = new Response.Listener<String>(){
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
                                UpdateComentariosRequest updatecomentariosRequest = new UpdateComentariosRequest(estado,comentario,responseListenerUpdateComentarios);
                                RequestQueue queue = Volley.newRequestQueue(ComentariosActivity.this);
                                queue.add(updatecomentariosRequest);
                            }
                            if(adapter.checkedComentarios.size()>0)
                            {
                                Toast.makeText(ComentariosActivity.this,sb.toString(),Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(ComentariosActivity.this,"Porfavor seleccione un comentario",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(ComentariosActivity.this,PrincipalActivity.class);
                            ComentariosActivity.this.startActivity(intent);
                        }
                    });
                    //fin funcionalidad del boton aprobar
                    //Inicio funcionalidad del boton eliminar
                    btn_eliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb=new StringBuffer();
                            for (Comentario_innerjoin_cliente co: adapter.checkedComentarios)
                            {
                                final int estado = 2;
                                final int comentario=co.getTay_comentario_id();
                                sb.append("Comentario "+comentario+" eliminado");
                                sb.append("\n");
                                //funcion del response
                                Response.Listener<String> responseListenerUpdateComentarios = new Response.Listener<String>(){
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
                                UpdateComentariosRequest updatecomentariosRequest = new UpdateComentariosRequest(estado,comentario,responseListenerUpdateComentarios);
                                RequestQueue queue = Volley.newRequestQueue(ComentariosActivity.this);
                                queue.add(updatecomentariosRequest);
                            }
                            if(adapter.checkedComentarios.size()>0)
                            {
                                Toast.makeText(ComentariosActivity.this,sb.toString(),Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(ComentariosActivity.this,"Porfavor seleccione un comentario",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(ComentariosActivity.this,PrincipalActivity.class);
                            ComentariosActivity.this.startActivity(intent);
                        }
                    });
                    //fin funcionalidad del boton eliminar
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        //fin funcion de l response
        ListarComentariosRequest listarcomentariosRequest = new ListarComentariosRequest(empresa,responseListenerComentarios);
        RequestQueue queue = Volley.newRequestQueue(ComentariosActivity.this);
        queue.add(listarcomentariosRequest);
    }

}
