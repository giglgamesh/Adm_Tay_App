package pe.oranch.restaurantroky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.entidades.Tay_servicio;

/**
 * Created by Daniel on 16/11/2017.
 */

public class MyAdapter extends ArrayAdapter<Tay_servicio> {
    private Context mContext;
    private ArrayList<Tay_servicio> listState;
    private MyAdapter myAdapter;
    private boolean isFromView = false;

    public MyAdapter(Context context, int resource, List<Tay_servicio> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<Tay_servicio>) objects;
        this.myAdapter = this;
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

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_layout, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.textoSpinner);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkboxSpinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTay_servicio_nombre());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();


            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}