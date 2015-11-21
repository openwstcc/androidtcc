package edu.fatec.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import edu.fatec.model.Usuario;
import edu.fatec.util.SenhaDialog;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private TextView viewEmail;
    private EditText editNome;
    private EditText editSobreNome;
    private EditText editTelefone;

    private Toolbar toolbar;

    private SenhaDialog senhaDialog;

    private SharedPreferences sharedPref;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        findViewsById();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        viewEmail.setText(usuario.getEmail());
        editNome.setText(usuario.getNome());
        editSobreNome.setText(usuario.getSobrenome());
        editTelefone.setText(usuario.getTelefone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.alterarSenha) {
            senhaDialog = new SenhaDialog(PerfilUsuarioActivity.this);
            senhaDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewEmail = (TextView) findViewById(R.id.viewEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editSobreNome = (EditText) findViewById(R.id.editSobreNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
    }

    private void volleyRequest() {
        String server = getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(PerfilUsuarioActivity.this);
        String url = server + "usuarios/alterarUsuario";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Suas informações foram atualizadas", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PerfilUsuarioActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Não foi possível atualizar suas informações de usuário", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String body = gson.toJson(usuario);
                persisteSharedPref(body);
                return body.getBytes();
            }
        };
        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuario.setNome(editNome.getText().toString());
        usuario.setSobrenome(editSobreNome.getText().toString());
        usuario.setTelefone(editTelefone.getText().toString());
        volleyRequest();
    }

    private void persisteSharedPref(String response) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor SharedPrefEdit = sharedPref.edit();
        SharedPrefEdit.putString("jsonUsuario", response);
        SharedPrefEdit.commit();
    }


}
