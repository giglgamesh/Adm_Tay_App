package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegistrarComidaAsocRequest extends StringRequest {

    private static final String REGISTRAR_COMIDAS_ASOCIADOS_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_COMIDAS_ASOCIADOS;
    private Map<String,String> params;
    public RegistrarComidaAsocRequest(int comidaas, int empresa, int estadocom, Response.Listener<String> listener){
        super(Method.POST, REGISTRAR_COMIDAS_ASOCIADOS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_tipocomida_id",comidaas+"");
        params.put ("tay_empresa_id",empresa+"");
        params.put ("tay_asoc_estado",estadocom+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
