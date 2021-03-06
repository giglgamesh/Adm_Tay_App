package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 15/11/2017.
 */

public class ServicioRequest extends StringRequest {
    private static final String SERVICIOS_EMPRESA_REQUEST_URL= Config.APP_API_URL + Config.SERVICIOS_EMPRESA;
    private Map<String,String> params;
    public ServicioRequest(int empresa,Response.Listener<String> listener){
        super(Method.POST, SERVICIOS_EMPRESA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_empresa_id",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
