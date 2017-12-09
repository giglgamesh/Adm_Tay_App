package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegistrarCartaRequest extends StringRequest {

    private static final String REGISTRAR_CARTA_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_CARTA;
    private Map<String,String> params;
    public RegistrarCartaRequest(String nombre, double precio, int empresa, String imagen, Response.Listener<String> listener){
        super(Method.POST, REGISTRAR_CARTA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_carta_nombre",nombre);
        params.put ("tay_carta_precio",precio+"");
        params.put ("tay_carta_empresa_id",empresa+"");
        params.put ("tay_carta_imagen",imagen);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
