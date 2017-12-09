package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateEmpresaImagenRequest extends StringRequest {

    private static final String ACTUALIZAR_EMPRESA_IMAGEN_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZA_EMPRESA_IMAGEN;
    private Map<String,String> params;
    public UpdateEmpresaImagenRequest(String imagen, int empresa, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_EMPRESA_IMAGEN_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_imagen",imagen);
        params.put ("tay_empresa_id",empresa+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
