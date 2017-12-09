package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 15/11/2017.
 */

public class ListarDescuentoRequest extends StringRequest {
    private static final String DESCUENTO_EMPRESA_REQUEST_URL= Config.APP_API_URL + Config.LISTA_DESCUENTO;
    private Map<String,String> params;
    public ListarDescuentoRequest(int empresa, Response.Listener<String> listener){
        super(Method.POST, DESCUENTO_EMPRESA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_descuento_empresa",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
