package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.activities.ComentariosActivity;
import pe.oranch.restaurantroky.activities.ComidaActivity;
import pe.oranch.restaurantroky.entidades.Comentario_innerjoin_cliente;
import pe.oranch.restaurantroky.entidades.Tay_menu;
import pe.oranch.restaurantroky.listeners.ItemClickListener;
import pe.oranch.restaurantroky.listeners.OnClickListener;
import pe.oranch.restaurantroky.request.UpdateComentariosRequest;
import pe.oranch.restaurantroky.request.UpdateMenuRequest;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_entradasAdapter extends RecyclerView.Adapter<Tay_entradasAdapter.Tay_menuHolder> {
    List<Tay_menu> listaEntradas;
    List<Tay_menu> listaEntradasEliminadas;
    private Tay_entradasAdapter adaptador;
    private Context mContext;

    public Tay_entradasAdapter(Context context, List<Tay_menu> listaEntradas) {
        this.listaEntradas = listaEntradas;
        this.mContext= context;
        this.adaptador = this;
    }

    @Override
    public Tay_entradasAdapter.Tay_menuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_entradas,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_entradasAdapter.Tay_menuHolder(vista);
    }

    @Override
    public void onBindViewHolder(final Tay_entradasAdapter.Tay_menuHolder holder, final int position) {
        holder.textoentrada.setText(listaEntradas.get(position).getTay_menu_nombre().toString());

        holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estado = 0;
                int menu;
                holder.textoentrada.setText(listaEntradas.get(position).getTay_menu_id().toString());
                menu = Integer.parseInt(listaEntradas.get(position).getTay_menu_id().toString());
                //EXPERIMENTO 01
                Response.Listener<String> responseListenerUpdateMenu = new Response.Listener<String>(){
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
                UpdateMenuRequest updatemenuRequest = new UpdateMenuRequest(estado,menu,responseListenerUpdateMenu);
                RequestQueue queue = Volley.newRequestQueue(mContext);
                queue.add(updatemenuRequest);
                //FINAL EXPERIMENTO
                listaEntradas.remove(position);
                //notifyItemRemoved(position);
                adaptador.notifyDataSetChanged();
                ComidaActivity comida=new ComidaActivity();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaEntradas.size();
    }

    public class Tay_menuHolder extends RecyclerView.ViewHolder{

        EditText textoentrada;
        ImageView addentradas;
        LinearLayout botonEliminar;
        public Tay_menuHolder(View itemView) {
            super(itemView);
            textoentrada = (EditText) itemView.findViewById(R.id.textoEntrada);
            addentradas = (ImageView) itemView.findViewById(R.id.addEntradas);
            botonEliminar = (LinearLayout) itemView.findViewById(R.id.botonEliminar);
        }
    }
}
