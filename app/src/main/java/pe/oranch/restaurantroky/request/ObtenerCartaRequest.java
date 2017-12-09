package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 15/11/2017.
 */

public class ObtenerCartaRequest extends StringRequest {
    private static final String OBTENER_CARTA_REQUEST_URL= Config.APP_API_URL + Config.OBTENER_CARTA;
    private Map<String,String> params;
    public ObtenerCartaRequest(int empresa, Response.Listener<String> listener){
        super(Method.POST, OBTENER_CARTA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_carta_empresa_id",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
