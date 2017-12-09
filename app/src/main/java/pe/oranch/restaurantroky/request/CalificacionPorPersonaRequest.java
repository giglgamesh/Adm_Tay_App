package pe.oranch.restaurantroky.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 11/11/2017.
 */

public class CalificacionPorPersonaRequest extends StringRequest {
    private static final String CALIFICACION_POR_PERSONA_REQUEST_URL= Config.APP_API_URL + Config.CALIFICACION_POR_PERSONA;
    private Map<String,String> params;
    public CalificacionPorPersonaRequest(int idusuario, int idcalificacion, Response.Listener<String> listener){
        super(Request.Method.POST, CALIFICACION_POR_PERSONA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_empresa_id",idusuario+"");
        params.put ("tay_calificacion_calificacion",idcalificacion+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
