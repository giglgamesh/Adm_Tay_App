package pe.oranch.restaurantroky.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pe.oranch.restaurantroky.R;
import pe.oranch.restaurantroky.request.RegisterRequest;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etnombre, etusuario, etpassword ;
    Button btn_registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etnombre = (EditText) findViewById(R.id.EdtT_nombre);
        etusuario = (EditText) findViewById(R.id.EdtT_usuario);
        etpassword = (EditText) findViewById(R.id.EdtT_password);

        btn_registrar = (Button) findViewById(R.id.Btn_registrar);

        btn_registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String name=etnombre.getText().toString();
        final String username=etusuario.getText().toString();
        final String password=etpassword.getText().toString();

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success= jsonReponse.getBoolean("success");

                    if (success){
                        Intent intent = new Intent(RegistroActivity.this,MainActivity.class);
                        RegistroActivity.this.startActivity(intent);
                    }else {
                        AlertDialog.Builder builder= new AlertDialog.Builder(RegistroActivity.this);
                        builder.setMessage("Error al registrar")
                        .setNegativeButton("Retry",null)
                        .create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name,username,password, respoListener);
        RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
        queue.add(registerRequest);
    }
}
