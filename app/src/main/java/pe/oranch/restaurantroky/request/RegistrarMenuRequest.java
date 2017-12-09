package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class RegistrarMenuRequest extends StringRequest {

    private static final String REGISTRAR_MENU_REQUEST_URL= Config.APP_API_URL + Config.REGISTRAR_MENU;
    private Map<String,String> params;
    public RegistrarMenuRequest(int menutipo, String menunombre, double menuprecio, String menufecha, int menuempresa, Response.Listener<String> listener){
        super(Method.POST, REGISTRAR_MENU_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_menu_tipo",menutipo+"");
        params.put ("tay_menu_nombre",menunombre);
        params.put ("tay_menu_precio",menuprecio+"");
        params.put ("tay_menu_fecha",menufecha);
        params.put ("tay_menu_empresa",menuempresa+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
