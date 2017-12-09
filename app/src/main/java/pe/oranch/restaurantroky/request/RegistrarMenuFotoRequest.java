package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegistrarMenuFotoRequest extends StringRequest {

    private static final String REGISTRAR_MENU_FOTO_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_MENU_FOTO;
    private Map<String,String> params;
    public RegistrarMenuFotoRequest(String nombre, int estado, String fecha, double precio, int empresa, String imagen, Response.Listener<String> listener){
        super(Method.POST, REGISTRAR_MENU_FOTO_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_menu_dia_nombre",nombre);
        params.put ("tay_menu_dia_estado",estado+"");
        params.put ("tay_menu_dia_fecha",fecha);
        params.put ("tay_menu_dia_precio",precio+"");
        params.put ("tay_menu_dia_empresa",empresa+"");
        params.put ("tay_menu_dia_foto",imagen);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
