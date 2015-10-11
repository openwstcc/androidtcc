package edu.fatec.activity;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import edu.fatec.model.Usuario;

public class LoginActivity extends Activity {

    private Usuario u = new Usuario();
    private Button login;
    private EditText email;
    private EditText senha;
    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        server = getString(R.string.wstcc);
        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                         String url = server + "wstcc/usuarios/loginUsuario";
                                         StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                 new Response.Listener<String>() {
                                                     @Override
                                                     public void onResponse(String response) {
                                                         Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                     }
                                                 }, new Response.ErrorListener() {
                                             @Override
                                             public void onErrorResponse(VolleyError error) {
                                                 Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService:" + error.toString(), Toast.LENGTH_SHORT).show();
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
        u.setEmail(email.getText().toString());
        u.setSenha(senha.getText().toString());
        Gson gson = new GsonBuilder().create();
        return gson.toJson(u);
    }

    private void findViewsById() {
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
