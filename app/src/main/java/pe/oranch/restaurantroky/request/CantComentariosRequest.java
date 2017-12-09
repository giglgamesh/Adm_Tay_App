package pe.oranch.restaurantroky.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 09/11/2017.
 */

public class CantComentariosRequest extends StringRequest {
    private static final String CANTIDAD_COMENTARIOS_REQUEST_URL= Config.APP_API_URL + Config.CANTIDAD_COMENTARIOS;
    private Map<String,String> params;
    public CantComentariosRequest(int idusuario, Response.Listener<String> listener){
        super(Request.Method.POST, CANTIDAD_COMENTARIOS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_comentario_empresa",idusuario+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
