package pe.oranch.restaurantroky.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.restaurantroky.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class UpdateEmpresaRequest extends StringRequest {

    private static final String ACTUALIZAR_EMPRESA_REQUEST_URL= Config.APP_API_URL + Config.ACTUALIZA_EMPRESA;
    private Map<String,String> params;
    public UpdateEmpresaRequest(int tcomida, String direccion,String referencia,String telefono,String entrada,String salida, int empresa, Response.Listener<String> listener){
        super(Method.POST, ACTUALIZAR_EMPRESA_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_empresa_tipocomida",tcomida+"");
        params.put ("tay_empresa_direccion",direccion);
        params.put ("tay_empresa_referencia",referencia);
        params.put ("tay_empresa_telefono",telefono);
        params.put ("tay_empresa_horainicial",entrada);
        params.put ("tay_empresa_horafin",salida);
        params.put ("tay_empresa_id",empresa+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
