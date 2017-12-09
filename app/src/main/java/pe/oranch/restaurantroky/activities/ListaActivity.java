package pe.oranch.restaurantroky.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.oranch.restaurantroky.BottomNavigationViewHelper;
import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.entidades.Usuario;
import pe.oranch.restaurantroky.request.ListaRequest;
import pe.oranch.restaurantroky.adapter.UsuariosAdapter;
/**
 * Created by Daniel on 15/11/2017.
 */

public class ListaActivity extends AppCompatActivity {
    ArrayList<Usuario> listaUsuarios;
    RecyclerView recyclerUsuarios;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_demo);

        obtenerDatos( );
    }

    private void obtenerDatos() {
        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
            try {
                listaUsuarios = new ArrayList<>();
                recyclerUsuarios = findViewById(R.id.idRecycler);
                recyclerUsuarios.setLayoutManager(new LinearLayoutManager(ListaActivity.this));
                recyclerUsuarios.setHasFixedSize(true);

                JSONObject jsonReponse = new JSONObject(response);
                Usuario usuario=null;
                JSONArray json=jsonReponse.optJSONArray("usuario");
                for (int i=0;i<json.length();i++){
                    usuario=new Usuario();
                    JSONObject jsonObject=null;
                    jsonObject=json.getJSONObject(i);

                    usuario.setDocumento(jsonObject.optInt("documento"));
                    usuario.setNombre(jsonObject.optString("nombre"));
                    usuario.setProfesion(jsonObject.optString("profesion"));
                    listaUsuarios.add(usuario);
                }
                UsuariosAdapter adapter=new UsuariosAdapter(listaUsuarios);
                recyclerUsuarios.setAdapter(adapter);
            }catch (JSONException e){
                e.printStackTrace();
            }
            }
        };
        ListaRequest listaRequest = new ListaRequest(responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(ListaActivity.this);
        queue.add(listaRequest);
    }
}
