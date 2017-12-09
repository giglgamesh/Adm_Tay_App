package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.activities.ComidaActivity;
import pe.oranch.restaurantroky.entidades.Tay_carta;
import pe.oranch.restaurantroky.request.UpdateCartaRequest;
import pe.oranch.restaurantroky.request.UpdateMenuRequest;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_cartaAdapter extends RecyclerView.Adapter<Tay_cartaAdapter.Tay_menuHolder> {
    List<Tay_carta> listaCartas;
    private Tay_cartaAdapter adaptador;
    private Context mContext;

    public Tay_cartaAdapter(Context context, List<Tay_carta> listaCartas) {
        this.listaCartas = listaCartas;
        this.mContext= context;
        this.adaptador = this;
    }

    @Override
    public Tay_cartaAdapter.Tay_menuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_carta,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_cartaAdapter.Tay_menuHolder(vista);
    }

    @Override
    public void onBindViewHolder(final Tay_cartaAdapter.Tay_menuHolder holder, final int position) {
        holder.txtplatocarta.setText(listaCartas.get(position).getTay_carta_nombre().toString());
        holder.edittextprecio.setText(listaCartas.get(position).getTay_carta_precio().toString());

        holder.btn_eliminar_plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estado = 0;
                int menu;
                menu = Integer.parseInt(listaCartas.get(position).getTay_carta_id().toString());
                //EXPERIMENTO 01
                Response.Listener<String> responseListenerUpdateCarta = new Response.Listener<String>(){
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
                UpdateCartaRequest updatecartaRequest = new UpdateCartaRequest(estado,menu,responseListenerUpdateCarta);
                RequestQueue queue = Volley.newRequestQueue(mContext);
                queue.add(updatecartaRequest);
                //FINAL EXPERIMENTO
                listaCartas.remove(position);
                //notifyItemRemoved(position);
                adaptador.notifyDataSetChanged();
                ComidaActivity comida=new ComidaActivity();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCartas.size();
    }

    public class Tay_menuHolder extends RecyclerView.ViewHolder{

        EditText txtplatocarta, edittextprecio;
        Button btn_eliminar_plato;
        public Tay_menuHolder(View itemView) {
            super(itemView);
            txtplatocarta = (EditText) itemView.findViewById(R.id.txtPlatoCarta);
            edittextprecio = (EditText) itemView.findViewById(R.id.editTextPrecio);
            btn_eliminar_plato = (Button) itemView.findViewById(R.id.Btn_eliminar_plato);
        }
    }
}
