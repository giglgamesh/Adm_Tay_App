package pe.oranch.restaurantroky.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.adapter.Tay_servicioAdapter;
import pe.oranch.restaurantroky.entidades.Tay_servicio;
import pe.oranch.restaurantroky.request.ServicioRequest;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Lista2Activity extends AppCompatActivity {
    ArrayList<Tay_servicio> listaServicios;
    RecyclerView recyclerUsuarios;

    //variable del archivo de sesion
    private SharedPreferences pref;
    //fin archivo de sesion

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_demo2);

        obtenerDatos( );
    }

    private void obtenerDatos() {
        pref = PreferenceManager.getDefaultSharedPreferences(Lista2Activity.this.getBaseContext());
        final int empresa= Integer.parseInt(String.valueOf(pref.getInt("tay_empresa_id", 0)));

        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
            try {
                listaServicios = new ArrayList<>();
                recyclerUsuarios = findViewById(R.id.idRecycler);
                recyclerUsuarios.setLayoutManager(new LinearLayoutManager(Lista2Activity.this));
                recyclerUsuarios.setHasFixedSize(true);

                JSONObject jsonReponse = new JSONObject(response);
                Tay_servicio tay_servicio=null;
                JSONArray json=jsonReponse.optJSONArray("usuario");
                for (int i=0;i<json.length();i++){
                    tay_servicio=new Tay_servicio();
                    JSONObject jsonObject=null;
                    jsonObject=json.getJSONObject(i);

                    tay_servicio.setTay_servicio_nombre(jsonObject.optString("tay_servicio_nombre"));
                    listaServicios.add(tay_servicio);
                }
                Tay_servicioAdapter adapter=new Tay_servicioAdapter(Lista2Activity.this,listaServicios);
                recyclerUsuarios.setAdapter(adapter);
            }catch (JSONException e){
                e.printStackTrace();
            }
            }
        };
        ServicioRequest servicioRequest = new ServicioRequest(empresa,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(Lista2Activity.this);
        queue.add(servicioRequest);
    }
}
