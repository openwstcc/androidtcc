package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.fatec.model.Usuario;

public class LoginActivity extends Activity {

    private Usuario u = new Usuario();
    private Usuario jsonUsuario;

    private Button login;
    private Button registre;
    private EditText email;
    private EditText senha;
    private LinearLayout infoLogin;
    private TextView textInfoLogin;
    private ProgressBar progressBarLogin;
    private String server;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewsByID();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        if (sharedUsuario.length() > 1) {
            Intent i = new Intent(LoginActivity.this, MainTestActivity.class);
            startActivity(i);
        }

        server = getString(R.string.wstcc);

        registre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UsuarioTestActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLogin.setVisibility(View.VISIBLE);
                progressBarLogin.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                String url = server + "wstcc/usuarios/loginUsuario";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("RESPONSE", response);
                                Usuario usuario = new Gson().fromJson(response, Usuario.class);
                                if (usuario.getNome()!=null) {
                                    persisteSharedPref(response);
                                    Intent i = new Intent(LoginActivity.this, MainTestActivity.class);
                                    startActivity(i);
                                } else {
                                    infoLogin.setBackgroundColor(Color.parseColor("#ff4444"));
                                    textInfoLogin.setText("Usuário e senha incorretos");
                                    progressBarLogin.setVisibility(View.GONE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        infoLogin.setBackgroundColor(Color.parseColor("#ff4444"));
                        textInfoLogin.setText("Não foi possível realizar o login, tente novamente");
                        progressBarLogin.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return jsonUsuario().getBytes();
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    private void findViewsByID() {
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        login = (Button) findViewById(R.id.loginUsuario);
        registre = (Button) findViewById(R.id.registreUsuario);
        infoLogin = (LinearLayout) findViewById(R.id.infoLogin);
        textInfoLogin = (TextView) findViewById(R.id.textInfoLogin);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

    }

    private String jsonUsuario() {
        u.setEmail(email.getText().toString());
        u.setSenha(senha.getText().toString());
        Gson gson = new GsonBuilder().create();
        return gson.toJson(u);
    }

    private void persisteSharedPref(String response) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor SharedPrefEdit = sharedPref.edit();
        SharedPrefEdit.putString("jsonUsuario", response);
        SharedPrefEdit.commit();
    }

}
