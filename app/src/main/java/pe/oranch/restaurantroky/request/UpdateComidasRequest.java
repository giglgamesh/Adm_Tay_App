package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateComidasRequest extends StringRequest {

    private static final String ACTUALIZAR_COMIDAS_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZA_COMIDAS;
    private Map<String,String> params;
    public UpdateComidasRequest(int estado, int comida, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_COMIDAS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_asoc_estado",estado+"");
        params.put ("tay_asoc_tipc_id",comida+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
