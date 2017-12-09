package pe.oranch.restaurantroky.activities;

import android.content.SharedPreferences;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.SpannableString;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.request.LoguinRequest;
import pe.oranch.restaurantroky.utilities.Utils;


public class MainActivity extends AppCompatActivity {
    private ProgressDialog prgDialog;
    TextView tv_registrar;
    EditText et_usuario, et_password;
    private SpannableString loginString;
    Button btn_log;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Realmente desea salir?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            System.exit(0);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_registrar = (TextView) findViewById(R.id.tvregistrar);
        et_usuario = (EditText) findViewById(R.id.Tv_usuario);
        et_password = (EditText) findViewById(R.id.Tv_password);

        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(MainActivity.this,RegistroActivity.class);
                MainActivity.this.startActivity(intentReg);
            }
        });
        btn_log = (Button) findViewById(R.id.Btn_iniciar);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = et_usuario.getText().toString();
                final String password = et_password.getText().toString();
                if (inputValidation()){
                    if(isInternetOn()) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonReponse = new JSONObject(response);
                                    boolean success= jsonReponse.getBoolean("success");

                                    if (success){
                                        //retorna el valor de nombre de la base de datos en el jason
                                        String idempresa = jsonReponse.getString("tay_empresa_id");
                                        String nombre = jsonReponse.getString("tay_empresa_nombre");
                                        String tipocomida = jsonReponse.getString("tay_empresa_tipocomida");
                                        String empresadireccion = jsonReponse.getString("tay_empresa_direccion");
                                        String horainicial = jsonReponse.getString("tay_empresa_horainicial");
                                        String horafinal = jsonReponse.getString("tay_empresa_horafin");
                                        String telefono = jsonReponse.getString("tay_empresa_telefono");
                                        String latitud = jsonReponse.getString("tay_empresa_latitud");
                                        String longitud = jsonReponse.getString("tay_empresa_longitud");
                                        String referencia = jsonReponse.getString("tay_empresa_referencia");
                                        String perfil = jsonReponse.getString("tay_ruta_imagen");
                                        //fin retorno

                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext());
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("tay_empresa_id", Integer.parseInt(idempresa));
                                        editor.putString("tay_empresa_nombre", nombre);
                                        editor.putInt("tay_empresa_tipocomida", Integer.parseInt(tipocomida));
                                        editor.putString("tay_empresa_direccion", empresadireccion);
                                        editor.putString("tay_empresa_horainicial", horainicial);
                                        editor.putString("tay_empresa_horafin", horafinal);
                                        editor.putString("tay_empresa_telefono", telefono);
                                        editor.putString("tay_empresa_latitud", latitud);
                                        editor.putString("tay_empresa_longitud", longitud);
                                        editor.putString("tay_empresa_referencia", referencia);
                                        editor.putString("tay_ruta_imagen", perfil);
                                        editor.apply();

                                        //iniciar actividad
                                        Intent intent = new Intent(MainActivity.this,PrincipalActivity.class);
                                        //finalizar actividad
                                        //enviar valor
                                        intent.putExtra("tay_empresa_nombre", nombre);
                                        //fin envio de valor

                                        MainActivity.this.startActivity(intent);
                                    }else {
                                        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                                        builder.setMessage("Error al Loguear")
                                                .setNegativeButton("Retry",null)
                                                .create().show();
                                    }
                                } catch (JSONException e) {
                                    prgDialog.cancel();
                                    Utils.psLog("Login Fail : " + e.getMessage());
                                    e.printStackTrace();
                                    showFailPopup();
                                    e.printStackTrace();
                                }

                            }
                        };

                        LoguinRequest loguinRequest = new LoguinRequest(username, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(loguinRequest);
                    } else {

                        showOffline();;

                    }
                }
            }
        });
    }
    private void initData() {
        try {
            loginString = Utils.getSpannableString(getString(R.string.login));
        }catch(Exception e){
            Utils.psErrorLogE("Error in init data.", e);
        }
    }
    private void showFailPopup() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.login);
        builder.setMessage(R.string.login_fail);
        builder.setPositiveButton(R.string.OK, null);
        builder.show();
    }
    private void showOffline() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.sorry_title);
        builder.setMessage(R.string.device_offline);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {Utils.psLog("OK clicked.");
            }
        });
        builder.show();
    }
    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
    private boolean inputValidation() {

        if(et_usuario.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this.getApplicationContext(), R.string.usuario_mensaje_validacion,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(et_password.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this.getApplicationContext(), R.string.password_mensaje_validacion,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
}
