package edu.fatec.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;

import edu.fatec.model.Usuario;

public class UsuarioTestActivity extends Activity {
    private Usuario usuario = new Usuario();

    private Button inserirUsuario;
    private EditText nome;
    private EditText sobreNome;
    private EditText telefone;
    private EditText dataNasc;
    private EditText email;
    private EditText senha;

    private DatePickerDialog dataPicker;
    private SimpleDateFormat dateFormatter;

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_test);

        findViewsById();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        setDateTimeField();

        server = getString(R.string.wstcc);

        dataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                dataPicker.show();
            }
        });

        inserirUsuario.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Toast.makeText(getApplicationContext(), novoUsuario(), Toast.LENGTH_LONG).show();

                  RequestQueue queue = Volley.newRequestQueue(UsuarioTestActivity.this);
                  String url = server + "wstcc/usuarios/inserirUsuario";

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
        usuario.setNome(nome.getText().toString());
        usuario.setSobrenome(sobreNome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        usuario.setTelefone(telefone.getText().toString());
        try {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            usuario.setDataNasc(sdf.parse(dataNasc.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        usuario.setPerfil("A");

        Gson gson = new GsonBuilder().create();
        return gson.toJson(usuario);
    }

    private void findViewsById() {
        nome = (EditText) findViewById(R.id.nome);
        sobreNome = (EditText) findViewById(R.id.sobreNome);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        telefone = (EditText) findViewById(R.id.telefone);
        dataNasc = (EditText) findViewById(R.id.dataNasc);
        dataNasc.setInputType(InputType.TYPE_NULL);
        inserirUsuario = (Button) findViewById(R.id.inserirUsuario);
    }

    public void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        dataPicker = new DatePickerDialog(UsuarioTestActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dataNasc.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dataNasc.getWindowToken(),0);
    }

}
