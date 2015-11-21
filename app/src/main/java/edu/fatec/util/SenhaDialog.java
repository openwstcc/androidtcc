package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import edu.fatec.activity.LoginActivity;
import edu.fatec.model.Usuario;

public class SenhaDialog extends Dialog {
    private Activity c;
    private EditText senhaNova;
    private EditText senhaConfirma;
    private ProgressBar progressBar;

    private Button cancelaSenha;
    private Button atualizaSenha;

    private Usuario usuario;

    private SharedPreferences sharedPref;

    public SenhaDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_senha);

        findViewsById();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        usuario = new Gson().fromJson(sharedUsuario, Usuario.class);


        cancelaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        atualizaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validator())
                    return;
                volleyRequest();
            }
        });
    }

    public void closeDialog() {
        this.dismiss();
    }

    public void findViewsById() {
        senhaNova = (EditText) findViewById(R.id.senhaNova);
        senhaConfirma = (EditText) findViewById(R.id.senhaConfirma);
        cancelaSenha = (Button) findViewById(R.id.cancelaSenha);
        atualizaSenha = (Button) findViewById(R.id.atualizaSenha);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSenha);
    }

    private boolean validator() {
        if (!validaSenha(senhaNova))
            return false;
        else if (!validaConfirmaSenha(senhaNova, senhaConfirma))
            return false;
        else
            return true;
    }

    private boolean validaSenha(View view) {
        EditText senha = (EditText) view;
        if (TextUtils.isEmpty(senha.getText())) {
            senha.setError("Insira uma senha");
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

    private boolean validaConfirmaSenha(View viewSenha, View viewSenhaConfirma) {
        EditText senha = (EditText) viewSenha;
        EditText senhaConfirma = (EditText) viewSenhaConfirma;
        if (TextUtils.isEmpty(senhaConfirma.getText())) {
            senhaConfirma.setError("Insira uma senha");
            senhaConfirma.setFocusable(true);
            return false;
        } else if (!senha.getText().toString().equals(senhaConfirma.getText().toString())) {
            senhaConfirma.setError("Senha não são iguais");
            senha.setFocusable(true);
            return false;
        } else
            return true;
    }

    private void volleyRequest() {
        usuario.setSenha(senhaNova.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        String server = c.getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(c);
        String url = server + "usuarios/alterarSenha";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(c.getApplicationContext(), "Senha atualizada", Toast.LENGTH_SHORT).show();
                        closeDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c.getApplicationContext(), "Não foi possível atualizar a senha. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String body = gson.toJson(usuario);
                return body.getBytes();
            }
        };
        queue.add(stringRequest);
    }

}
