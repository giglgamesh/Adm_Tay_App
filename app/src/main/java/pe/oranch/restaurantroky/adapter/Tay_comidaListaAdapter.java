package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.activities.ComentariosActivity;
import pe.oranch.restaurantroky.entidades.Tay_comida;
import pe.oranch.restaurantroky.entidades.Tay_servicio;
import pe.oranch.restaurantroky.entidades.Tay_servicioInner;
import pe.oranch.restaurantroky.listeners.ItemClickListener;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_comidaListaAdapter extends ArrayAdapter<Tay_comida> {

    StringBuffer sb=null;
    ArrayList<Tay_comida> listacomida;
    public ArrayList<Tay_comida> checkedComidas = new ArrayList<>();
    public ArrayList<Tay_comida> checkedComidasEliminados = new ArrayList<>();
    private Context mContext;
    private Tay_comidaListaAdapter tay_comidaListaAdapter;
    private boolean isFromView = false;

    public Tay_comidaListaAdapter(Context context, int resource,  List<Tay_comida> listaServicios) {
        super(context, resource, listaServicios);
        this.listacomida = (ArrayList<Tay_comida>)listaServicios;
        this.mContext = context;
        this.tay_comidaListaAdapter = this;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final Tay_comidaListaAdapter.ComidaHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.lista_comidas, null);
            Spinner.LayoutParams layoutParams = new Spinner.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(layoutParams);
            holder = new Tay_comidaListaAdapter.ComidaHolder();

            holder.textocomidas= (TextView) convertView.findViewById(R.id.textoComidas);
            convertView.setTag(holder);

        } else {
            holder = (Tay_comidaListaAdapter.ComidaHolder) convertView.getTag();
        }


        holder.textocomidas.setText((listacomida.get(position).getTay_tipocomida_nombre().toString()));


        //holder.textocomidas.setText(listacomida.get(position).getTay_tipocomida_nombre());

        // To check weather checked event fire from getview() or user input
        //isFromView = true;
        //holder.checkboxcomidas.setChecked(listacomida.get(position).isSelected());
        //isFromView = false;

        //funcionabilidada checkbox
        //fin funcionalidad

        //if ((position == 0)) {
        //    holder.checkboxcomidas.setVisibility(View.INVISIBLE);
        //} else {
        //    holder.checkboxcomidas.setVisibility(View.VISIBLE);
        //}

        return convertView;
    }

    public class ComidaHolder {
        TextView textocomidas;
    }

}
