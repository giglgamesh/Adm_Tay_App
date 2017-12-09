package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class DescuentoActualizarRequest extends StringRequest {

    private static final String ACTUALIZAR_DESCUENTO_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZAR_DESCUENTO;
    private Map<String,String> params;
    public DescuentoActualizarRequest(int descuento, String fechaini, String fechafin, String descripcion, int descuentoid, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_DESCUENTO_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_descuento_descuento",descuento+"");
        params.put ("tay_descuento_fecha",fechaini);
        params.put ("tay_descuento_fechafin",fechafin);
        params.put ("tay_descuento_descripcion",descripcion);
        params.put ("tay_descuento_id",descuentoid+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
