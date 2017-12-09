package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 15/11/2017.
 */

public class ListarComentariosRequest extends StringRequest {
    private static final String COMENTARIOS_EMPRESA_REQUEST_URL= Config.APP_API_URL + Config.LISTA_COMENTARIOS;
    private Map<String,String> params;
    public ListarComentariosRequest(int empresa,Response.Listener<String> listener){
        super(Method.POST, COMENTARIOS_EMPRESA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_empresa_id",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
