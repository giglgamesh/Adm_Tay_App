package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.entidades.Comentario_innerjoin_cliente;
import pe.oranch.restaurantroky.listeners.ItemClickListener;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Comentario_innerjoin_clienteAdapter extends RecyclerView.Adapter<Comentario_innerjoin_clienteAdapter.Comentario_innerjoin_clienteHolder> {


    ArrayList<Comentario_innerjoin_cliente> listacomentario;
    public ArrayList<Comentario_innerjoin_cliente> checkedComentarios = new ArrayList<>();

    public Comentario_innerjoin_clienteAdapter(ArrayList<Comentario_innerjoin_cliente> listaComentario) {
        this.listacomentario = listaComentario;
    }

    @Override
    public Comentario_innerjoin_clienteAdapter.Comentario_innerjoin_clienteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comentarios,null);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Comentario_innerjoin_clienteAdapter.Comentario_innerjoin_clienteHolder(vista);
    }

    @Override
    public void onBindViewHolder(Comentario_innerjoin_clienteAdapter.Comentario_innerjoin_clienteHolder holder, int position) {
        holder.txtnombrepersona.setText(listacomentario.get(position).getTay_cliente_nombre().toString()+" "+listacomentario.get(position).getTay_comentario_id().toString());
        holder.txtcomentario.setText(listacomentario.get(position).getTay_comentario_descripcion().toString());


        //funcionabilidada checkbox
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox checkboxcomentario = (CheckBox) v;
                //REVISAR SI EL CHECK ESTA O NO INDICADO
                if(checkboxcomentario.isChecked())
                {
                    checkedComentarios.add(listacomentario.get(pos));
                }else  if(!checkboxcomentario.isChecked())
                {
                    checkedComentarios.remove(listacomentario.get(pos));
                }
            }
        });
        //fin funcionalidad
    }


    @Override
    public int getItemCount() {
        return listacomentario.size();
    }

    public class Comentario_innerjoin_clienteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtnombrepersona, txtcomentario;
        CheckBox checkboxcomentario;
        ItemClickListener itemClickListener;
        public Comentario_innerjoin_clienteHolder(View itemView) {
            super(itemView);
            txtnombrepersona= (TextView) itemView.findViewById(R.id.txtNombrePersona);
            txtcomentario= (TextView) itemView.findViewById(R.id.txtComentario);
            checkboxcomentario = (CheckBox) itemView.findViewById(R.id.checkboxComentario);
            checkboxcomentario.setOnClickListener(this);
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
