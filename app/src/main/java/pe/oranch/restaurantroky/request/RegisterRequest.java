package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_REQUEST;
    private Map<String,String> params;
    public RegisterRequest(String nombre, String usuario, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_empresa_nombre",nombre);
        params.put ("tay_empresa_usuario",usuario);
        params.put ("tay_empresa_clave",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
