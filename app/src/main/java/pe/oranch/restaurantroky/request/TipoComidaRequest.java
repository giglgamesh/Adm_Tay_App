package pe.oranch.restaurantroky.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 13/11/2017.
 */

public class TipoComidaRequest extends StringRequest {
    private static final String TIPO_COMIDA_REQUEST_URL= Config.APP_API_URL + Config.TIPO_COMIDA;
    private Map<String,String> params;
    public TipoComidaRequest(int idcomida, Response.Listener<String> listener){
        super(Request.Method.POST, TIPO_COMIDA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_tipocomida_id",idcomida+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
