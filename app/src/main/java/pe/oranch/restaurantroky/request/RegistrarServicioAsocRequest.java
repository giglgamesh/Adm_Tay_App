package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegistrarServicioAsocRequest extends StringRequest {

    private static final String REGISTRAR_SERVICIOS_ASOCIADOS_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_SERVICIOS_ASOCIADOS;
    private Map<String,String> params;
    public RegistrarServicioAsocRequest(int servicioser, int empresa, int estadoser, Response.Listener<String> listener){
        super(Method.POST, REGISTRAR_SERVICIOS_ASOCIADOS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_servicio_id",servicioser+"");
        params.put ("tay_empresa_id",empresa+"");
        params.put ("tay_asoc_estado",estadoser+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
