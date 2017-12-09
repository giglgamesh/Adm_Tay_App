package pe.oranch.restaurantroky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.entidades.Comentario_innerjoin_cliente;
import pe.oranch.restaurantroky.entidades.Tay_servicio;
import pe.oranch.restaurantroky.entidades.Tay_servicioInner;
import pe.oranch.restaurantroky.listeners.ItemClickListener;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_servicioListaAdapter extends RecyclerView.Adapter<Tay_servicioListaAdapter.ServiciosHolder> {


    ArrayList<Tay_servicioInner> listaservicios;
    public ArrayList<Tay_servicioInner> checkedServicios = new ArrayList<>();
    public ArrayList<Tay_servicioInner> checkedServiciosEliminados = new ArrayList<>();

    public Tay_servicioListaAdapter(ArrayList<Tay_servicioInner> listaServicios) {
        this.listaservicios = listaServicios;
    }

    @Override
    public Tay_servicioListaAdapter.ServiciosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_servicios,null);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_servicioListaAdapter.ServiciosHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_servicioListaAdapter.ServiciosHolder holder, int position) {
        //holder.textoservicios.setText(listaservicios.get(position).getTay_servicio_nombre().toString()+"SC");
        if ((listaservicios.get(position).getTay_asoc_estado().toString()).equals("1")){
            //holder.textoservicios.setText(listaservicios.get(position).getTay_servicio_nombre().toString()+"SI"+(listaservicios.get(position).getTay_asoc_serv_id()));
            holder.textoservicios.setText(listaservicios.get(position).getTay_servicio_nombre().toString());
            holder.checkboxservicios.setChecked(true);
            checkedServicios.add(listaservicios.get(position));
        }else{
            //holder.textoservicios.setText(listaservicios.get(position).getTay_servicio_nombre().toString()+"NO"+(listaservicios.get(position).getTay_asoc_serv_id()));
            holder.textoservicios.setText(listaservicios.get(position).getTay_servicio_nombre().toString());
            holder.checkboxservicios.setChecked(false);
            checkedServiciosEliminados.add(listaservicios.get(position));
        }
        //funcionabilidada checkbox
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox checkboxservicios = (CheckBox) v;
                //REVISAR SI EL CHECK ESTA O NO INDICADO
                if(checkboxservicios.isChecked())
                {
                    checkedServicios.add(listaservicios.get(pos));
                    checkedServiciosEliminados.remove(listaservicios.get(pos));
                }else  if(!checkboxservicios.isChecked())
                {
                    checkedServicios.remove(listaservicios.get(pos));
                    checkedServiciosEliminados.add(listaservicios.get(pos));
                }
            }
        });
        //fin funcionalidad
    }


    @Override
    public int getItemCount() {
        return listaservicios.size();
    }

    public class ServiciosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textoservicios;
        CheckBox checkboxservicios;
        ItemClickListener itemClickListener;
        public ServiciosHolder(View itemView) {
            super(itemView);
            textoservicios= (TextView) itemView.findViewById(R.id.textoServicios);
            checkboxservicios = (CheckBox) itemView.findViewById(R.id.checkboxServicios);
            checkboxservicios.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
