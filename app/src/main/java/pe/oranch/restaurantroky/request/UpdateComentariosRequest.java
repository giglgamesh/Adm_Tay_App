package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateComentariosRequest extends StringRequest {

    private static final String ACTUALIZAR_COMENTARIO_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZA_COMENTARIOS;
    private Map<String,String> params;
    public UpdateComentariosRequest(int estado, int comentario, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_COMENTARIO_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_comentario_estado",estado+"");
        params.put ("tay_comentario_id",comentario+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
