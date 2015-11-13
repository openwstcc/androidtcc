package edu.fatec.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.reflect.TypeToken;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.model.Materia;
import edu.fatec.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private Usuario u = new Usuario();
    private String server;

    private Button login;
    private Button registre;
    private EditText email;
    private EditText senha;
    private LinearLayout infoLogin;
    private TextView textInfoLogin;
    private ProgressBar progressBarLogin;
    private Toolbar toolbar;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor SharedPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewsByID();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPrefEdit = sharedPref.edit();

        String todasMaterias = sharedPref.getString("jsonMaterias", "");
        if (todasMaterias.length() < 1) {
            volleyBuscarMaterias();
        }

        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        if (sharedUsuario.length() > 1) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

        server = getString(R.string.wstcc);

        registre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, NovoUsuarioActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validaEmail(email))
                    return;
                else if (!validaSenha(senha))
                    return;
                volleyLogin();
            }
        });
    }

    private void findViewsByID() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    public void volleyBuscarMaterias() {
        server = getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = server + "materias/buscarMaterias";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Materia>>() {
                        }.getType();
                        List<Materia> materiasJson = new Gson().fromJson(response, listType);
                        SharedPrefEdit.putString("jsonMaterias", response);
                        SharedPrefEdit.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService. Tente Novamente.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    public void volleyLogin() {
        infoLogin.setVisibility(View.VISIBLE);
        infoLogin.setBackgroundColor(getResources().getColor(R.color.colorWarning));
        textInfoLogin.setText("Realizando login");
        progressBarLogin.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        String url = server + "usuarios/loginUsuario";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        Usuario usuario = new Gson().fromJson(response, Usuario.class);
                        if (usuario.getNome() != null) {
                            persisteSharedPref(response);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            infoLogin.setBackgroundColor(getResources().getColor(R.color.colorFail));
                            textInfoLogin.setText("Usuário e senha incorretos");
                            progressBarLogin.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoLogin.setBackgroundColor(getResources().getColor(R.color.colorFail));
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

    public static boolean validaEmail(View view) {
        EditText email = (EditText) view;
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Email inválido");
            email.setFocusable(true);
            return false;
        } else if (email.getText().length() < 5) {
            email.setError("Email inválido. Tamanho mínimo de 5 caracteres.");
            email.setFocusable(true);
            return false;
        } else if (email.getText().length() > 60) {
            email.setError("Email inválido. Tamanho máximo de 60 caracteres");
            email.setFocusable(true);
            return false;
        } else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }

    public static boolean validaSenha(View view) {
        EditText senha = (EditText) view;
        if (TextUtils.isEmpty(senha.getText())) {
            senha.setError("Senha inválida");
            senha.setFocusable(true);
            return false;
        } else if (senha.getText().length() < 5) {
            senha.setError("Senha inválida. Tamanho mínimo de 5 caracteres.");
            senha.setFocusable(true);
            return false;
        } else if (senha.getText().length() > 60) {
            senha.setError("Senha inválida. Tamanho máximo de 60 caracteres");
            senha.setFocusable(true);
            return false;
        } else
            return true;
    }

}
