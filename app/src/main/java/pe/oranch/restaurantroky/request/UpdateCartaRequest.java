package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateCartaRequest extends StringRequest {

    private static final String ACTUALIZAR_ESTADO_CARTA_REQUEST_URL= Config.APP_API_URL + Config.ESTADO_CARTA;
    private Map<String,String> params;
    public UpdateCartaRequest(int estado, int menu, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_ESTADO_CARTA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_carta_foto_estado",estado+"");
        params.put ("tay_carta_id",menu+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
