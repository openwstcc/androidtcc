package edu.fatec.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.fatec.model.Usuario;

public class UsuarioTestActivity extends Activity {
    private Usuario usuario = new Usuario();
    private Button inserirUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_test);

        inserirUsuario = (Button) findViewById(R.id.inserirUsuario);

        inserirUsuario.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Toast.makeText(getApplicationContext(), novoUsuario(), Toast.LENGTH_LONG).show();

                  RequestQueue queue = Volley.newRequestQueue(UsuarioTestActivity.this);
                  String url = "http://192.168.167.118:8080/wstcc/usuarios/inserirUsuario";

                  StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                          new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {
                                  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                              }
                          }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {                      Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService:"+error.toString(), Toast.LENGTH_SHORT).show();
                      }
                  }) {
                      @Override
                      public byte[] getBody() throws AuthFailureError {
                          return novoUsuario().getBytes();
                      }
                  };
                  queue.add(stringRequest);
              }
      }
        );
    }

    public String novoUsuario() {
        usuario.setNome(((EditText) findViewById(R.id.nome)).getText().toString());
        usuario.setSobrenome(((EditText) findViewById(R.id.sobreNome)).getText().toString());
        usuario.setEmail(((EditText) findViewById(R.id.email)).getText().toString());
        usuario.setSenha(((EditText) findViewById(R.id.senha)).getText().toString());
        usuario.setTelefone(((EditText) findViewById(R.id.telefone)).getText().toString());
        usuario.setPerfil("A");
        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            long time = smf.parse(((EditText) findViewById(R.id.dataNasc)).getText().toString()).getTime();
            Date dataNasc = new Date(time);
            usuario.setDataNasc(dataNasc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        return gson.toJson(usuario);
    }

}
