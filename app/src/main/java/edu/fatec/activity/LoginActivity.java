package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.widget.Toast;

import edu.fatec.model.Usuario;

public class LoginActivity extends Activity {

    private Usuario u = new Usuario();
    private Usuario jsonUsuario;

    private Button login;
    private Button registre;
    private EditText email;
    private EditText senha;
    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        server = getString(R.string.wstcc);

        registre = (Button) findViewById(R.id.registreUsuario);

        registre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,UsuarioTestActivity.class);
                startActivity(i);
            }
        });

        login = (Button) findViewById(R.id.loginUsuario);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                String url = server + "wstcc/usuarios/loginUsuario";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                jsonUsuario = new Gson().fromJson(response, Usuario.class);
                                Toast.makeText(getApplicationContext(), jsonUsuario.toString(), Toast.LENGTH_SHORT).show();
                                persisteSharedPref();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService:" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return JsonUsuario().getBytes();
                    }
                };
                queue.add(stringRequest);
            }
        });

    }

    private String JsonUsuario() {
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        u.setEmail(email.getText().toString());
        u.setSenha(senha.getText().toString());
        Gson gson = new GsonBuilder().create();
        return gson.toJson(u);
    }

    private void persisteSharedPref() {
        SharedPreferences SharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor SharedPrefEdit = SharedPref.edit();
        SharedPrefEdit.putString("jsonUsuario", jsonUsuario.toString());
        SharedPrefEdit.commit();
    }

}
