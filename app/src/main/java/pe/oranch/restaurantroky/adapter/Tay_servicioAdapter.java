package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.entidades.Usuario;
import pe.oranch.restaurantroky.entidades.Tay_servicio;

import static pe.oranch.restaurantroky.Config.APP_API_URL;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_servicioAdapter extends RecyclerView.Adapter<Tay_servicioAdapter.Tay_servicioHolder> {
    List<Tay_servicio> listaServicio;
    private Context mContext;

    public Tay_servicioAdapter(Context context, List<Tay_servicio> listaServicio) {
        this.listaServicio = listaServicio;
        this.mContext= context;
    }

    @Override
    public Tay_servicioAdapter.Tay_servicioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.field2,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_servicioAdapter.Tay_servicioHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_servicioAdapter.Tay_servicioHolder holder, int position) {
        holder.txtservicio.setText(listaServicio.get(position).getTay_servicio_nombre().toString());


        for (int i=0;i<listaServicio.size();i++){
            String urlimagen;
            urlimagen = APP_API_URL + (listaServicio.get(position).getTay_servicio_imagenurl().toString());
            Picasso.with(mContext).load(urlimagen).into(holder.imgservicio);
        }


    }

    @Override
    public int getItemCount() {
        return listaServicio.size();
    }

    public class Tay_servicioHolder extends RecyclerView.ViewHolder{

        TextView txtservicio;
        ImageView imgservicio;

        public Tay_servicioHolder(View itemView) {
            super(itemView);
            txtservicio = (TextView) itemView.findViewById(R.id.txtServicio);
            imgservicio = (ImageView) itemView.findViewById(R.id.imgServicio);
        }
    }
}
