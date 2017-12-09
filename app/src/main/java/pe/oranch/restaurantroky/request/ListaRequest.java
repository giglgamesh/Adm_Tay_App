package pe.oranch.restaurantroky.request;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 15/11/2017.
 */

public class ListaRequest extends StringRequest {
    private static final String LISTA_USUARIOS_REQUEST_URL= Config.APP_API_URL + Config.LISTA_USUARIOS;
    private Map<String,String> params;
    public ListaRequest(Response.Listener<String> listener){
        super(Request.Method.GET, LISTA_USUARIOS_REQUEST_URL,listener,null);
        params = new HashMap<>();
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}
