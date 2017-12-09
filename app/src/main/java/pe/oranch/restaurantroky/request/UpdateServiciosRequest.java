package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateServiciosRequest extends StringRequest {

    private static final String ACTUALIZAR_SERVICIOS_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZA_SERVICIOS;
    private Map<String,String> params;
    public UpdateServiciosRequest(int estado, int servicio, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_SERVICIOS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_asoc_estado",estado+"");
        params.put ("tay_asoc_serv_id",servicio+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
